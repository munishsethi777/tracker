<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>My Locations</title>
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;
    key=ABQIAAAAcl" type="text/javascript"></script>
<script type="text/javascript">

function load() 
{
      if (GBrowserIsCompatible()) {
        var point;
        var map=new GMap2(document.getElementById("map"));
         map.addControl(new GOverviewMapControl());
     map.enableDoubleClickZoom();
     map.enableScrollWheelZoom();
     map.addControl(new GMapTypeControl());
     map.addControl(new GSmallMapControl());
         var address='<img src="myimage.gif" width=150 height=40/><br/>' + 
         <font size="2" face="Arial"><b>INDIA</b><br/><br/>Home.<br/>' + 
     New York City<br/><br/>America<br/>Ph.: 23743823</font>';
         var marker = new GMarker(point);
         map.setCenter(point,17);
         map.addOverlay(marker);
         map.setMapType(G_HYBRID_MAP);
         GEvent.addListener(marker, "click", function() {
            marker.openInfoWindowHtml(address);});
         marker.openInfoWindowHtml(address); 
               
      }
    }
 </script>
<body onload="load();" onunload="GUnload()" style=" 
        background-color:Transparent">
<div id="map" style="width: 900px; height: 500px"></div>
</body>
</html>