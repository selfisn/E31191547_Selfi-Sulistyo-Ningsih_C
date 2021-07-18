<?php
 
class DbOperation
{
    //Database connection link
    private $con;
 
    //Class constructor
    function __construct()
    {
        //Getting the DbConnect.php file
        require_once dirname(__FILE__) . '/DbConnect.php';
 
        //Creating a DbConnect object to connect to the database
        $db = new DbConnect();
 
        //Initializing our connection link of this class
        //by calling the method connect of DbConnect class
        $this->con = $db->connect();
    }

    function login($email, $password)
    {
        $stmt = $this->con->prepare("SELECT * FROM pelanggan WHERE email_pelanggan = '$email' AND password_pelanggan = '$password'");
        $stmt->bind_result($id_pelanggan, $email_pelanggan, $password_pelanggan, $nama_pelanggan, $telepon_pelanggan, $alamat_pelanggan);
        $stmt->execute();

        $detail = array(); 

        while($stmt->fetch()){
            $data  = array();
            $data['id_pelanggan'] = $id_pelanggan;
            $data['email_pelanggan'] = $email_pelanggan;
            $data['password_pelanggan'] = md5($password_pelanggan);
            $data['nama_pelanggan'] = $nama_pelanggan;
            $data['telepon_pelanggan'] = $telepon_pelanggan;
            $data['alamat_pelanggan'] = $alamat_pelanggan;

            array_push($detail, $data); 
        }

        $stmt->close();

        return $detail;
    }

    function register($email, $password)
    {
        $queryCheck = $this->con->prepare("SELECT * FROM pelanggan WHERE email_pelanggan = '$email'");
        $queryCheck->execute();
        $isAlready = $queryCheck->fetch();
        $queryCheck->close();

        // Jika Sudah Ada
        if ($isAlready) {
            return false;
        } else {
            $reg = $this->con->prepare("INSERT INTO pelanggan (email_pelanggan, password_pelanggan, nama_pelanggan, telepon_pelanggan, alamat_pelanggan) VALUES ('$email', '$password', '-', '-', '-')");
            $reg->execute();
            $reg->close();

            return true;
        }
    }
    
    function getPembelianProdukDetail($id_pembelian, $id_produk)
    {
        $stmt = $this->con->prepare("SELECT jumlah, harga, berat FROM pembelian_produk WHERE id_pembelian = '$id_pembelian' AND id_produk = '$id_produk'");
        $stmt->bind_result($jumlah, $harga, $berat);
        $stmt->execute();

        $detail = array(); 

        while($stmt->fetch()){
            $data  = array();
            $data['jumlah'] = $jumlah;
            $data['harga'] = $harga;
            $data['berat'] = $berat;

            array_push($detail, $data); 
        }

        $stmt->close();

        return $detail;
    }

    function addToCart($id_pembelian, $id_pelanggan, $id_produk, $jumlah, $nama, $harga, $berat, $subberat, $subharga){
        // Mengecek apakah id_pembelian sudah ada atau belum
        $check = $this->con->prepare("SELECT * FROM pembelian WHERE id_pembelian = '$id_pembelian'");
        $check->execute();
        $data = $check->fetch();
        $check->close();

        
        // Jika Sudah Ada
        if ($data) {
            // Mengecek apakah id_barang pada tabel pembelian_produk sudah ada atau belum
            $check_barang = $this->con->prepare("SELECT * FROM pembelian_produk WHERE id_pembelian = '$id_pembelian' AND id_produk = '$id_produk'");
            $check_barang->execute();
            $isBarangAda = $check_barang->fetch();
            $check_barang->close();

            // Jika barang sudah ditambahkan sebelumnya
            if ($isBarangAda) {
                // Mendapatkan detail pembelian saat ini
                $jumlahSaatIni = $this->getPembelianProdukDetail($id_pembelian, $id_produk)[0]['jumlah'];
                $harga_produk = $this->getPembelianProdukDetail($id_pembelian, $id_produk)[0]['harga'];
                $berat_produk = $this->getPembelianProdukDetail($id_pembelian, $id_produk)[0]['berat'];

                $jumlahAkhir = $jumlahSaatIni + $jumlah;
                $subHargaAkhir = $harga_produk * $jumlahAkhir;
                $subBeratAkhir = $berat_produk * $jumlahAkhir;


                $update_barang = $this->con->prepare("UPDATE pembelian_produk SET jumlah = '$jumlahAkhir', subberat = '$subBeratAkhir', subharga = '$subHargaAkhir' WHERE id_pembelian = '$id_pembelian' AND id_produk = '$id_produk'");
                $update_barang->execute();
                $update_barang->close();
            } else {
                $sub_berat = $berat * $jumlah;
                $sub_harga = $harga * $jumlah;
                $stmt = $this->con->prepare("INSERT INTO pembelian_produk (id_pembelian, id_produk, jumlah, nama, harga, berat, subberat, subharga) VALUES ('$id_pembelian', '$id_produk', '$jumlah', '$nama', '$harga', '$berat', '$sub_berat', '$sub_harga')");
                $stmt->execute();

            }

            return true; 

        } else {
            // Jika Belum Ada
            $stmt = $this->con->prepare("INSERT INTO pembelian (id_pembelian, id_pelanggan, id_ongkir, tanggal_pembelian, total_pembelian, nama_lokasi, tarif, alamat_pengiriman, resi_pengiriman) VALUES ('$id_pembelian', '$id_pelanggan', '1', DATE_FORMAT(NOW(), '%Y-%m-%d'), '0', '-', '0', '-', '-')");
            $stmt->execute();
            $stmt->close();

            $sub_berat = $berat * $jumlah;
            $sub_harga = $harga * $jumlah;

            $stmt2 = $this->con->prepare("INSERT INTO pembelian_produk (id_pembelian, id_produk, jumlah, nama, harga, berat, subberat, subharga) VALUES ('$id_pembelian', '$id_produk', '$jumlah', '$nama', '$harga', '$berat', '$sub_berat', '$sub_harga')");
            $stmt2->execute();
            $stmt2->close();

            return true; 
        }
    }

    function addToCartDirect($id_pembelian, $id_pelanggan, $id_produk, $jumlah, $nama, $harga, $berat, $subberat, $subharga){
        $sub_berat = $berat * $jumlah;
        $sub_harga = $harga * $jumlah;
        $stmt = $this->con->prepare("INSERT INTO pembelian_produk (id_pembelian, id_produk, jumlah, nama, harga, berat, subberat, subharga) VALUES ('$id_pembelian', '$id_produk', '$jumlah', '$nama', '$harga', '$berat', '$sub_berat', '$sub_harga')");
        $stmt->execute();
    }

    function showCart($id_pembelian){
        $stmt = $this->con->prepare("SELECT id_pembelian_produk, id_pembelian, id_produk, SUM(jumlah) jumlah, nama, harga, berat, SUM(subberat) subberat, SUM(subharga) subharga FROM pembelian_produk WHERE id_pembelian='$id_pembelian' GROUP BY id_produk");
        $stmt->execute();
        $stmt->bind_result($id_pembelian_produk, $id_pembelian, $id_produk, $jumlah, $nama, $harga, $berat, $subberat, $subharga);

        $carts = array(); 

        while($stmt->fetch()){
            $cart  = array();
            $cart['id_pembelian_produk'] = $id_pembelian_produk;
            $cart['id_pembelian'] = $id_pembelian;
            $cart['id_produk'] = $id_produk;
            $cart['jumlah'] = $jumlah;
            $cart['nama'] = $nama;
            $cart['harga'] = $harga;
            $cart['berat'] = $berat;
            $cart['subberat'] = $subberat;
            $cart['subharga'] = $subharga;

            array_push($carts, $cart); 
        }

        $stmt->close();

        return $carts; 
    }

    function getTotalCart($id_pembelian)
    {
        $stmt = $this->con->prepare("SELECT SUM(subharga) total_harga, SUM(subberat) total_berat FROM pembelian_produk GROUP BY id_pembelian HAVING id_pembelian = '$id_pembelian'");
        $stmt->execute();
        $stmt->bind_result($total_harga, $total_berat);

        $totals = array(); 

        while($stmt->fetch()){
            $total  = array();
            $total['total_harga'] = $total_harga;
            $total['total_berat'] = $total_berat;

            array_push($totals, $total); 
        }

        $stmt->close();

        return $totals; 
    }

    function deleteItemFromCart($id_pembelian, $id_produk){
        $stmt = $this->con->prepare("DELETE FROM pembelian_produk WHERE id_pembelian = '$id_pembelian' AND id_produk = '$id_produk' ");
        if($stmt->execute())
            return true; 

        return false; 
    }

    function getNewProduk(){
        $stmt = $this->con->prepare("SELECT * FROM produk ORDER BY id_produk DESC LIMIT 6");
        $stmt->execute();
        $stmt->bind_result($id_produk, $id_kategori, $nama_produk, $harga_produk, $berat_produk, $foto_produk, $deskripsi_produk);

        $products = array(); 

        while($stmt->fetch()){
            $product  = array();
            $product['id_produk'] = $id_produk;
            $product['id_kategori'] = $id_kategori;
            $product['nama_produk'] = $nama_produk;
            $product['harga_produk'] = $harga_produk;
            $product['berat_produk'] = $berat_produk;
            $product['foto_produk'] = $foto_produk;
            $product['deskripsi_produk'] = $deskripsi_produk;

            array_push($products, $product); 
        }

        $stmt->close();

        return $products; 
    }
 
    function getProduk(){
        $stmt = $this->con->prepare("SELECT * FROM produk");
        $stmt->execute();
        $stmt->bind_result($id_produk, $id_kategori, $nama_produk, $harga_produk, $berat_produk, $foto_produk, $deskripsi_produk);

        $products = array(); 

        while($stmt->fetch()){
            $product  = array();
            $product['id_produk'] = $id_produk;
            $product['id_kategori'] = $id_kategori;
            $product['nama_produk'] = $nama_produk;
            $product['harga_produk'] = $harga_produk;
            $product['berat_produk'] = $berat_produk;
            $product['foto_produk'] = $foto_produk;
            $product['deskripsi_produk'] = $deskripsi_produk;

            array_push($products, $product); 
        }

        $stmt->close();

        return $products; 
    }

    function showNamaLokasi()
    {
        $stmt = $this->con->prepare("SELECT * FROM ongkir");
        $stmt->execute();
        $stmt->bind_result($id_ongkir, $nama_lokasi, $tarif);

        $allNamaLokasi = array(); 

        while($stmt->fetch()){
            $lokasi  = array();
            $lokasi['id_ongkir'] = $id_ongkir;
            $lokasi['nama_lokasi'] = $nama_lokasi;
            $lokasi['tarif'] = $tarif;

            array_push($allNamaLokasi, $lokasi); 
        }

        $stmt->close();

        return $allNamaLokasi;
    }

    function getOngkir($nama_lokasi)
    {
        $stmt = $this->con->prepare("SELECT * FROM ongkir WHERE nama_lokasi LIKE '%$nama_lokasi%'");
        $stmt->execute();
        $stmt->bind_result($id_ongkir, $nama_lokasi, $tarif);

        $ongkir = array(); 

        while($stmt->fetch()){
            $data  = array();
            $data['id_ongkir'] = $id_ongkir;
            $data['nama_lokasi'] = $nama_lokasi;
            $data['tarif'] = $tarif;

            array_push($ongkir, $data); 
        }

        $stmt->close();

        return $ongkir; 
    }

    function checkOut($id_pembelian, $id_pelanggan, $total_pembelian, $nama_lokasi, $alamat_pengiriman){
        $id_ongkir = $this->getOngkir($nama_lokasi)[0]['id_ongkir'];
        $tarif = $this->getOngkir($nama_lokasi)[0]['tarif'];

        // Mengecek
        $check_pembelian = $this->con->prepare("SELECT * FROM pembelian WHERE id_pembelian = '$id_pembelian'");
        $check_pembelian->execute();
        $isPembelianAda = $check_pembelian->fetch();
        $check_pembelian->close();

        // Jika 
        if ($isPembelianAda) {
            $total_pembelian = $this->getTotalCart($id_pembelian)[0]['total_harga'];

            $stmt = $this->con->prepare("UPDATE pembelian SET id_ongkir = '$id_ongkir', tanggal_pembelian = DATE_FORMAT(NOW(), '%Y-%m-%d'), total_pembelian = '$total_pembelian', nama_lokasi = '$nama_lokasi', tarif = '$tarif', alamat_pengiriman = '$alamat_pengiriman' WHERE id_pembelian = '$id_pembelian'");
            if($stmt->execute())
                return true; 
        } else {
              $stmt = $this->con->prepare("INSERT INTO pembelian (id_pembelian, id_pelanggan, id_ongkir, tanggal_pembelian, total_pembelian, nama_lokasi, tarif, alamat_pengiriman, resi_pengiriman) VALUES ('$id_pembelian', '$id_pelanggan', '$id_ongkir', DATE_FORMAT(NOW(), '%Y-%m-%d'), $total_pembelian, '$nama_lokasi', '$tarif', '$alamat_pengiriman', '-')");
            if($stmt->execute())
                return true; 
        }
    }

    function getDataPembelian()
    {
        $stmt = $this->con->prepare("SELECT * FROM pembelian");
        $stmt->execute();
        $stmt->bind_result($id_pembelian, $id_pelanggan, $id_ongkir, $tanggal_pembelian, $total_pembelian, $nama_lokasi, $tarif, $alamat_pengiriman, $status_pembelian, $resi_pengiriman);

        $data_pembelian = array(); 

        while($stmt->fetch()){
            $pembelian  = array();
            $pembelian['id_pembelian'] = $id_pembelian;
            $pembelian['id_pelanggan'] = $id_pelanggan;
            $pembelian['id_ongkir'] = $id_ongkir;
            $pembelian['tanggal_pembelian'] = $tanggal_pembelian;
            $pembelian['total_pembelian'] = $total_pembelian;
            $pembelian['nama_lokasi'] = $nama_lokasi;
            $pembelian['tarif'] = $tarif;
            $pembelian['alamat_pengiriman'] = $alamat_pengiriman;
            $pembelian['status_pembelian'] = $status_pembelian;
            $pembelian['resi_pengiriman'] = $resi_pengiriman;

            array_push($data_pembelian, $pembelian); 
        }

        $stmt->close();

        return $data_pembelian;
    }
}