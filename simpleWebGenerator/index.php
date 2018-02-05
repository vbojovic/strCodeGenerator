<?php
header("Cache-Control: no-store, no-cache, must-revalidate, max-age=0");
header("Cache-Control: post-check=0, pre-check=0", false);
header("Pragma: no-cache");
?>
<!DOCTYPE html>
<html lang="en"><head>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <!--<link rel="shortcut icon" href="http://getbootstrap.com/assets/ico/favicon.ico">-->

        <title>Simple Web Generator</title>

        <!-- Bootstrap core CSS -->
        <link href="jsCss/bootstrap.css" rel="stylesheet">

        <!-- Custom styles for this template -->
        <link href="jsCss/dashboard.css" rel="stylesheet">
        <link rel="stylesheet" href="jsCss/highlight/styles/sunburst.css">

        <!-- Just for debugging purposes. Don't actually copy this line! -->
        <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
          <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->
        <style type="text/css" id="holderjs-style"></style></head>

    <body>

        <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#">Project name</a>
                </div>
                <div class="navbar-collapse collapse">
                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="#">Dashboard</a></li>
                        <li><a href="#">Settings</a></li>
                        <li><a href="#">Profile</a></li>
                        <li><a href="#">Help</a></li>
                    </ul>
                    <form class="navbar-form navbar-right">
                        <input class="form-control" placeholder="Search..." type="text">
                    </form>
                </div>
            </div>
        </div>

        <div class="container-fluid">


            <div class="row">



                <div class="col-sm-3 col-md-2 sidebar">



                    <div class="panel-group" id="accordion">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h4 class="panel-title">
                                    <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                        Table list
                                    </a>
                                </h4>
                            </div>
                            <div id="collapseOne" class="panel-collapse collapse in">
                                <div class="panel-body">
                                    <p class="form-control">
                                        <a id='clearTablesA'>Clear</a>&nbsp;|&nbsp;
                                        <a id='jsonUploadA'>Load</a>
                                    </p>
                                    <p class="form-control" id="tableCheckBoxes">
                                        <a id='checkAllTables' action="check" component="tablesUl">Check all</a>&nbsp;|&nbsp;
                                        <a id='uncheckAllTables' action="uncheck" component="tablesUl">Uncheck all</a>&nbsp;|&nbsp;
                                        <a id='invertCheckedTables' action="invert" component="tablesUl">Invert</a>
                                    </p>
                                    <input type="text" placeholder="Search..." class="form-control" id="searchTables">    
                                    <ul class="nav nav-sidebar" id="tablesUl"></ul>
                                </div>  
                            </div>
                        </div>
                    </div>
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a data-toggle="collapse" data-parent="#accordion" href="#collapseThree">
                                    Function list
                                </a>
                            </h4>
                        </div>
                        <div id="collapseThree" class="panel-collapse collapse">
                            <div id="searchDiv">
                                <p class="form-control">ikone ideu ovde</p>
                                <input type="text" placeholder="Search..." class="form-control" id="searchFunctions">      
                            </div>  
                            <div class="panel-body">
                                <ul class="nav nav-sidebar" id="funcListUl">
                                    <!--<li class="active"><a href="#">Overview</a></li>-->
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>




            </div>









            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                <div id="templateDiv"></div>
                <div id="settingsDiv"></div>
                
                <div class="row" id="codeDiv" ></div>
            </div>


        </div>
    </div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="jsCss/jquery.js"></script>
    <script src="jsCss/bootstrap.js"></script>
    <script src="jsCss/docs.js"></script>

    <script src="https://cdn.rawgit.com/beautify-web/js-beautify/v1.6.14/js/lib/beautify.js"></script>
    <script src="https://cdn.rawgit.com/beautify-web/js-beautify/v1.6.14/js/lib/beautify-css.js"></script>
    <script src="https://cdn.rawgit.com/beautify-web/js-beautify/v1.6.14/js/lib/beautify-html.js"></script>
    <script type="text/javascript" src="jsCss/handlebars.js"></script>
    <!-- http://ejohn.org/blog/javascript-micro-templating/ -->
    <script type="text/javascript" src="jsCss/microtemplate_nocache.js"></script>
    <script type="text/javascript" src="jsCss/genericHelper.js"></script>
    <script type="text/javascript" src="jsCss/generator.js"></script>
    <script type="text/javascript" src="jsCss/uiHelper.js"></script>
    <script src="jsCss/highlight/highlight.pack.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
    <script type="text/javascript">
        var generator = new Generator(null);
        generator.setDbModel(null);

        var uiHelper = new UIHelper();
        var genericHelper = new GenericHelper();
    </script>
    <?php
    $templates = array("results", "dbList", "funcList", "jsonUpload", "loadTables");
    foreach ($templates as $template) {
        echo file_get_contents("templates/$template.html");
    }
    ?>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#homeMenu").click();
            $("body").on("click", function (e) {
                if (e.target.type != 'button')
                    return;
                
                var isCopyButton = (e.target.className.split(' ').indexOf('copy')>-1 );
                if (isCopyButton){
                    genericHelper.copyToClipboardWithBreaks('code:visible', 'li');                    
                }
                var isPageButton = (e.target.className.split(' ').indexOf('page')>-1 );
                if (isPageButton){
                    var id = parseInt(e.target.textContent)-1;
                    $('code').hide();
                    $('code:eq(' + id + ')').show();                    
                }
                
            });


        });
    </script>

</body></html>