<?php
header('Content-type:text/html;charset=utf-8');

$url = "http://jieone.com/demo/stock/data/stock.php?callback=jQuery17206472256606980518_1495438225734&Action=minute&stockID=002024&stockType=sz";
$params = array(
      "stock" => "",//股票名称
);
$paramstring = http_build_query($params);

$ch = curl_init();
curl_setopt( $ch , CURLOPT_URL , $url);
curl_setopt($ch,CURLOPT_RETURNTRANSFER,1);
curl_setopt($ch,CURLOPT_HEADER,0);
$response = curl_exec( $ch );
$arr=split(',',$response);
curl_close( $ch );
$r_arr = array();
$str = ' ';
$ii=0;
for($i=0;$i<sizeof($arr)-1;$i++){ 
    if($ii==6)
    {

        //echo $i.':    ';
      //echo $arr[$i].'</br>';
        $arr1 = split(':',$arr[$i]);
        $str1 = substr($arr1[0],0,strlen($arr1[0])-2);
        $str2 = substr($arr1[0], -2).':'.$arr1[1];
             $str = $str.','.$str1;
           //echo $str.'</br>';
           array_push($r_arr, $str);
           $str = $str2;
           $ii=0;
       }else
       {
        if($i==0)
        {
            $str = $arr[$i];
        }else{
            $str = $str.','.$arr[$i];
      }        
     }
     $ii++;
 }
 $r_str = $r_arr[0];
for($i=1;$i<sizeof($r_arr);$i++)
{
   echo $r_str;
   $r_str = $r_arr[$i];
   
}

?>  