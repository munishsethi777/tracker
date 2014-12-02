<? include("adminMenu.php");?>
<!DOCTYPE html>
<html lang="en">
<head>
<link rel="stylesheet" href="jqwidgets/styles/jqx.base.css" type="text/css" />
<link rel="stylesheet" href="jqwidgets/styles/jqx.arctic.css" type="text/css" />
<script type="text/javascript" src="jqwidgets/jqxcore.js"></script>
<script type="text/javascript" src="jqwidgets/jqxbuttons.js"></script>
<script type="text/javascript" src="jqwidgets/jqxscrollbar.js"></script>
<script type="text/javascript" src="jqwidgets/jqxlistbox.js"></script>
<script type="text/javascript" src="jqwidgets/jqxdropdownlist.js"></script>
<script type="text/javascript" src="jqwidgets/jqxmenu.js"></script>
<script type="text/javascript" src="jqwidgets/jqxdata.js"></script>
<script type="text/javascript" src="jqwidgets/jqxgrid.js"></script>
<script type="text/javascript" src="jqwidgets/jqxgrid.sort.js"></script>
<script type="text/javascript" src="jqwidgets/jqxgrid.selection.js"></script>
<script type="text/javascript" src="jqwidgets/jqxgrid.pager.js"></script>
<script type="text/javascript" src="jqwidgets/jqxgrid.filter.js"></script>
<script type="text/javascript" src="jqwidgets/jqxgrid.columnsreorder.js"></script>
<script type="text/javascript" src="jqwidgets/jqxgrid.columnsresize.js"></script>
<script type="text/javascript" src="jqwidgets/jqxdata.export.js"></script>
<script type="text/javascript" src="jqwidgets/jqxgrid.export.js"></script>
<link type="text/css" href="styles/bootstrap.css" rel="stylesheet" />

<script type="text/javascript">
        $(document).ready(function (){
            theme = "arctic";
            loadGrid(null);
            var url = 'ajaxAdminMgr.php?call=getActivityDataForGrid';
            $.getJSON(url, function(data){
                loadGrid(data);
                loadColsList(data.columns);
            });

            $("#excelExport").jqxButton({ theme: theme });
            $("#csvExport").jqxButton({ theme: theme });
            $("#htmlExport").jqxButton({ theme: theme });
            $("#excelExport").click(function () {
                $("#jqxgrid").jqxGrid('exportdata', 'xls', 'employees');
            });
            $("#csvExport").click(function () {
                $("#jqxgrid").jqxGrid('exportdata', 'csv', 'employees');
            });
            $("#htmlExport").click(function () {
                $("#jqxgrid").jqxGrid('exportdata', 'html', 'employees');
            });

        });
        function loadGrid(data){
            var columns = Array();
            var rows = Array();
            var dataFields = Array();
            if(data != null){
                columns = $.parseJSON(data.columns);
                rows = $.parseJSON(data.data);
                dataFields = $.parseJSON(data.datafields);
            }
            var source =
            {
                datatype: "json",
                id: 'id',
                pagesize: 20,
                localData: rows,
                datafields: dataFields
            };
            var dataAdapter = new $.jqx.dataAdapter(source);
            $("#jqxgrid").jqxGrid(
            {
                theme:'arctic',
                height: '100%',
                width: '100%',
                source: dataAdapter,
                filterable: true,
                sortable: true,
                autoshowfiltericon: true,
                columns: columns,
                pageable: true,
                autoheight: true,
                altrows: true,
                enabletooltips: true,
                columnsresize: true,
                columnsreorder: true
            });
        }

        function loadColsList(columns){
            var listSource = new Array;
            var columnsJson = $.parseJSON(columns);
            if(columnsJson != null){
                $.each(columnsJson ,function(index,value){
                    listSource.push($.parseJSON('{"label":"'+ this.text +'", "value": "'+ this.datafield +'", "checked":true}'));
                });
            }
            $("#jqxUserCustomFieldslistbox").jqxListBox({theme:'arctic', source: listSource, width: '100%', height: '100%',  checkboxes: true });
            $("#jqxUserCustomFieldslistbox").on('checkChange', function (event) {
                $("#jqxgrid").jqxGrid('beginupdate');
                if (event.args.checked) {
                    $("#jqxgrid").jqxGrid('showcolumn', event.args.value);
                }else {
                    $("#jqxgrid").jqxGrid('hidecolumn', event.args.value);
                }
                $("#jqxgrid").jqxGrid('endupdate');
            });
        }

        function loadModulesList(trainingsJson){
            var listSource = new Array;
            if(trainingsJson != null){
                $.each(trainingsJson ,function(index,value){
                    listSource.push($.parseJSON('{"label":"'+ this.title +'", "value": "'+ this.id +'", "checked":true}'));
                });
            }
            $("#jqxModuleslistbox").jqxListBox({theme:'arctic', source: listSource, width: '160px', height: 300,  checkboxes: true });

            $("#jqxModuleslistbox").on('checkChange', function (event) {
                $("#jqxgrid").jqxGrid('beginupdate');
                if (event.args.checked) {
                    $("#jqxgrid").jqxGrid('showcolumn', event.args.value);
                }else {
                    $("#jqxgrid").jqxGrid('hidecolumn', event.args.value);
                }
                $("#jqxgrid").jqxGrid('endupdate');
            });
        }
    </script>
</head>
<body class='default'>
    <div style="height:550px;">
        <div class="col-sm-2">
            <div id="jqxUserCustomFieldslistbox"></div>
            <div id="jqxModuleslistbox"></div>
        </div>
        <div class="col-sm-10">
            <div  id="jqxgrid"></div>
        </div>
    </div>
    <div style="margin:12px;">
        <input type="button" class="col-sm-1" value="Export to Excel" id='excelExport' />
        <input style="margin-left:8px;" type="button" class="col-sm-1" value="Export to CSV" id='csvExport' />
        <input style="margin-left:8px;" type="button" class="col-sm-1" value="Export to HTML" id='htmlExport' />
    </div>


</body>
</html>
