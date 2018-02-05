<?php

$sufixes = Array("delete","insert","update","view","select");
foreach ($sufixes as $sufix) {
    $fName = "<%%>_<%%>_{$sufix}.php";
    if (file_exists($fName)) unlink($fName);
}
$action=(isset($_POST['a']))?$_POST['a']:'log_in';

?>