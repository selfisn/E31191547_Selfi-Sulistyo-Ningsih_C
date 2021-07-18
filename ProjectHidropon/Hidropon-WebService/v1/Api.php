<?php 
 
 //getting the dboperation class
 require_once '../includes/DbOperation.php';

 header('Content-Type: application/json');
 
	function isTheseParametersAvailable($params){
		$available = true; 
		$missingparams = ""; 

		foreach($params as $param){
			if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
				$available = false; 
				$missingparams = $missingparams . ", " . $param; 
			}
		}

		if(!$available){
			$response = array(); 
			$response['error'] = true; 
			$response['message'] = 'Parameters ' . substr($missingparams, 1, strlen($missingparams)) . ' missing';

			//displaying error
			echo json_encode($response);

			//stopping further execution
			die();
		}
	}
 
$response = array();

if(isset($_GET['apicall'])){
 
switch($_GET['apicall']){

	case 'login':
		$db = new DbOperation();
		$data = $db->login(
			$_POST['email'],
			$_POST['password']
		);

		if ($data) {
			$response['error'] = false;
			$response['error_login'] = false;
			$response['message'] = 'Welcome!';
			$response['data_pelanggan'] = $data;
		} else {
			$response['error'] = false;
			$response['error_login'] = true;
			$response['message'] = 'Username atau Password Salah!';
		}
	break;

	case 'registeruser':
		$db = new DbOperation();
		$data = $db->register(
			$_POST['email'],
			$_POST['password']
		);

		if ($data) {
			$response['error'] = false;
			$response['error_reg'] = false;
			$response['message'] = 'Registrasi Berhasil, Silahkan Login!';
		} else {
			$response['error'] = false;
			$response['error_reg'] = true;
			$response['message'] = 'User Telah Terdaftar!';
		}
	break;

	case 'addtocart':
		isTheseParametersAvailable(array('id_pembelian', 'id_pelanggan', 'id_produk', 'jumlah', 'nama', 'harga', 'berat', 'subberat', 'subharga'));

		$db = new DbOperation();

		$result = $db->addToCart(
			$_POST['id_pembelian'],
			$_POST['id_pelanggan'],
			$_POST['id_produk'],
			$_POST['jumlah'],
			$_POST['nama'],
			$_POST['harga'],
			$_POST['berat'],
			$_POST['subberat'],
			$_POST['subharga']
		);


		if($result){
			$response['error'] = false; 
			$response['message'] = 'Berhasil menambah ke keranjang!';
			$response['heroes'] = $db->getProduk();
		}else{
			$response['error'] = true;
			$response['message'] = 'Some error occurred please try again';
		}
	break;

	case 'addtocartdirect':
		isTheseParametersAvailable(array('id_pembelian', 'id_pelanggan', 'id_produk', 'jumlah', 'nama', 'harga', 'berat', 'subberat', 'subharga'));

		$db = new DbOperation();

		$result = $db->addToCartDirect(
			$_POST['id_pembelian'],
			$_POST['id_pelanggan'],
			$_POST['id_produk'],
			$_POST['jumlah'],
			$_POST['nama'],
			$_POST['harga'],
			$_POST['berat'],
			$_POST['subberat'],
			$_POST['subharga']
		);


		if($result){
			$response['error'] = false; 
			$response['message'] = 'Berhasil melakukan pembelian langsung!!';
			$response['heroes'] = $db->getProduk();
		}else{
			$response['error'] = true;
			$response['message'] = 'Some error occurred please try again';
		}
	break;

	case 'getnewproducts':
		$db = new DbOperation();
		$response['error'] = false; 
		$response['message'] = 'Request successfully completed';
		$response['products'] = $db->getNewProduk();
	break;
 
	case 'getproduk':
		$db = new DbOperation();
		$response['error'] = false; 
		$response['message'] = 'Request successfully completed';
		$response['products'] = $db->getProduk();
	break;

	case 'getpembelianproduk':
		$db = new DbOperation();
		$data = $db->getPembelianProdukDetail(
			$_POST['id_pembelian'],
			$_POST['id_produk']
		);

		$response['error'] = false; 
		$response['message'] = 'Request successfully completed';
		$response['heroes'] = $data;
	break;


	case 'showcart':
		isTheseParametersAvailable(array('id_pembelian'));
		$db = new DbOperation();

		$result = $db->showCart(
			$_POST['id_pembelian']
		);

		$total = $db->getTotalCart(
			$_POST['id_pembelian']
		);

		 $response['error'] = false; 
		 $response['message'] = 'Request successfully completed';
		 $response['cart'] = $result;
		 $response['total'] = $total;
	break;

 	case 'deleteitemfromcart':
		$db = new DbOperation();

		$data = $db->deleteItemFromCart(
			$_POST['id_pembelian'],
			$_POST['id_produk']
		);

		$result = $db->showCart(
			$_POST['id_pembelian']
		);

		$total = $db->getTotalCart(
			$_POST['id_pembelian']
		);

		$response['error'] = false; 
		$response['message'] = 'Berhasil Menghapus Item!';
		$response['cart'] = $result;
		$response['total'] = $total;
	break;

	case 'getnamalokasi':
		$db = new DbOperation();
		$response['error'] = false; 
		$response['message'] = 'Request successfully completed';
		$response['lokasi'] = $db->showNamaLokasi();
	break;

	case 'checkout':
		// isTheseParametersAvailable(array('id_pembelian'));
		$db = new DbOperation();

		$checkoutData = $db->checkOut(
			$_POST['id_pembelian'],
			$_POST['id_pelanggan'],
			$_POST['total_pembelian'],
			$_POST['nama_lokasi'],
			$_POST['alamat_pengiriman']
		);

		if($checkoutData){
			$response['error'] = false; 
			$response['message'] = 'Berhasil melakukan checkout!';
		}else{
			$response['error'] = true; 
			$response['message'] = 'Some error occurred please try again';
		}
	break;

	case 'getdatapembelian':
		$db = new DbOperation();

		$result = $db->getDataPembelian();

		$response['error'] = false; 
		$response['message'] = 'Request successfully completed';
		$response['pembelian'] = $result;
	break;
 
}
 
}else{
	$response['error'] = true; 
	$response['message'] = 'Invalid API Call';
}

echo json_encode($response);