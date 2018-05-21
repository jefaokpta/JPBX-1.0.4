<%-- 
    Document   : testebootstrap
    Created on : 10/01/2013, 16:40:54
    Author     : jefaokpta
--%>

<%@page import="com.jpbx.Controller"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.jpbx.Asterisk"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
    <%
       Controller head=new Controller();
       out.print(head.header("JPBX->Status"));
       
        if(session.getAttribute("return")!=null){
            out.print("<script>"+
    	       "$(function(){"+
                    "$('#myModal').modal('show')"+
                "});"+
                "</script>");
            out.print(" <div id=\"alert\">");
            out.print("<div id=\"myModal\" class=\"modal hide fade\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">");
            out.print("<div class=\"modal-header\">");
            out.print("<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">×</button>");
            out.print("<h3 id=\"myModalLabel\">JPBX Informa</h3> </div>");
            out.print("<div class=\"modal-body\">");
            out.print("<p>"+session.getAttribute("return") +"</p></div>");
            //out.print("<div class=\"modal-footer\">");
            //out.print("<button class=\"btn\" data-dismiss=\"modal\" aria-hidden=\"true\">Fechar</button>");
            //out.print("<button class=\"btn btn-primary\">Salvar mudanças</button>");
            out.print("</div></div>");
            session.removeAttribute("return");
        }
        if(session.getAttribute("lvl").equals("Administrador"))
            if(session.getAttribute("bkp")!=null){
                out.print("<script>"+
                   "$(function(){"+
                        "$('#myModal').modal('show')"+
                    "});"+
                    "</script>");
                out.print(" <div id=\"alert\">");
                out.print("<div id=\"myModal\" class=\"modal hide fade\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">");
                out.print("<div class=\"modal-header\">");
                out.print("<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">×</button>");
                out.print("<h3 id=\"myModalLabel\">JPBX Informa</h3> </div>");
                out.print("<div class=\"modal-body\">");
                out.print("<h5>É necessário executar as otimizações semanais do sistema.<br>Deseja executar agora?</h5></div>");
                out.print("<div class=\"modal-footer\">");
                out.print("<button class=\"btn\" data-dismiss=\"modal\" aria-hidden=\"true\">Fechar</button>");
                out.print("<button id=\"execBkp\" data-dismiss=\"modal\" aria-hidden=\"true\" class=\"btn btn-primary\">Executar</button>");
                out.print("</div></div></div>");
                session.removeAttribute("bkp");
            }
        if(session.getAttribute("user")==null){
            response.sendRedirect("logout.jsp?error=Desconectado por inatividade.");
            return;
        }
     %>
        <script src="css/highchart/highcharts.js"></script>
        <script src="css/highchart/highcharts-more.js"></script>
        <script src="css/highchart/exporting.js"></script>
  </head>

  <body>
      <div id="bkp"></div>
      <%out.print(head.navigator(session.getAttribute("lvl").toString()));%>
       
       <div id="container" class="container table-bordered">
          <h3>Status do <% Asterisk ast=new Asterisk(); 
          String totalHD,usedHD;
          ast.AstConnect();
          String ver=ast.getInfos("core show version").get(0);
            out.print(ver.substring(ver.indexOf("A"), ver.indexOf("b")));%></h3>
          <table class="span11 table-striped table-condensed table-bordered">
              
          <% //Tabela Asterisk
           //Iterator it=ast.getInfos("core show uptime").iterator();
            
            List list=ast.getInfos("core show uptime");
            String up=(String)list.get(0),restart=(String)list.get(1);
            out.print("<tr><td>Tempo em atividade</td><td>"+up.substring(up.indexOf(":")+1) +"</td></tr>"+
                    "<tr><td>Última Atualização (módulos)</td><td>"+restart.substring(restart.indexOf(":")+1)+"</td></tr>");
            list=ast.getInfos("core show channels verbose");
            ast.AstDisconnect();
            int tList=list.size();
            String chan=(String)list.get(tList-3),calls=(String)list.get(tList-2),
                    process=(String)list.get(tList-1);
            out.print("<tr><td>Canais Ativos</td><td>"+chan.substring(0, chan.indexOf("active")) +"</td></tr>"+
                    "<tr><td>Em Conversação</td><td>"+calls.substring(0, calls.indexOf("active"))+"</td></tr>"+
                    "<tr><td>Chamadas Processadas</td><td>"+process.substring(0, process.indexOf("call"))+"</td></tr>");
          %>
      </table>
      <% //Tabela Armazenamento
                          
            String ls_str;
           // List listLs=new ArrayList();
            Process ls_proc=Runtime.getRuntime().exec("cat /etc/lsb-release");
	    // get its output (your input) stream
            list.removeAll(list);
	    DataInputStream ls_in = new DataInputStream(
                                          ls_proc.getInputStream());
            
		while ((ls_str = ls_in.readLine()) != null) {
                    list.add(ls_str);
		}
                String version=(String) list.get(3);
                version=version.substring(version.indexOf("\"")+1);
                version=version.replace("\"", "");
                //zerando variaveis
                ls_str=null;
                list.removeAll(list);
                out.print("<h3>Status do Servidor "+version+"</h3>");
                out.print("<div class=\"span11 table-bordered\">");
                out.print("<div id=\"cpu\" style=\"width: 160px; height: 190px;float: left\"></div>");
                out.print("<div id=\"ram\" class\"span3\" style=\"width: 160px; height: 190px;float: left\"></div>");
                out.print("<div id=\"swap\" class\"span3\" style=\"width: 160px; height: 190px;float: left\"></div>");
                out.print("<div id=\"hw\" class=\"span3\" style=\"width: 550px; height: 190px;float: left\"></div>");
                out.print("</div>");
                //out.print("<div class=\"clearfix\"></div>");
                //out.print("<a href=\"#\" onclick=\"details();\">+Detalhes</a>");
                out.print("<div id=\"details\" class=\"span11 table-bordered\">");
                out.print("<h4>Especificações do Servidor</h4>");
                out.print("<table class=\"span3 table-condensed table-striped table-bordered\">");
                out.print("<tr><th style=\"background-color: gray\">Armazenamento</th><th style=\"background-color: gray\"></th></tr>");
                //redeclarando tudo
                ls_proc=Runtime.getRuntime().exec("df -h");
                ls_in=new DataInputStream(ls_proc.getInputStream());

                while ((ls_str=ls_in.readLine()) != null) {
                   list.add(ls_str);
		}
                String data=(String)list.get(1);
                data=data.substring(15);
                data=data.trim();
                data=data.replaceAll(" ", "s");
                data=data.replace("s/", "");
                data="<tr><td>Total de HD</td><td>"+data.replaceFirst("ss", "</td></tr><tr><td>Usado</td><td>");
                String mod=data;
                totalHD=mod.substring(mod.indexOf("D")+10, mod.indexOf("G"));
                mod=mod.replaceFirst("G", "");
                 usedHD=mod.substring(mod.indexOf("U")+14, mod.indexOf("G"));
               //  out.print("<!--"+totalHD+" "+usedHD+"-->"); //
                data=data.replaceFirst("sss", "</td></tr><tr><td>Livre</td><td>");
                data=data.replaceFirst("ss", "</td></tr><tr><td>Percentual de Uso</td><td>");
                
                out.print(data+"</td></tr>");
          %>
       </table>
      <table class="table-striped span3 table-condensed table-bordered">
          <tr><th style="background-color: gray">Memória (MB)</th><th style="background-color: gray">Total</th>
              <th style="background-color: gray">Usado</th><th style="background-color: gray">Livre</th></tr>
          <% //Tabela memoria
            list.removeAll(list);
            ls_proc=Runtime.getRuntime().exec("free -m");
                ls_in=new DataInputStream(ls_proc.getInputStream());

                while ((ls_str=ls_in.readLine()) != null) {
                   list.add(ls_str);
		}
                String ram=(String)list.get(1),cache=(String)list.get(2),swap=(String)list.get(3);
                ram=ram.substring(ram.indexOf(":")+1).trim();
                out.print("<tr><td>RAM</td><td>"+ram.substring(0,7)+"</td><td>"+ram.substring(8,16)+"</td><td>"+ram.substring(16,29)+"</td></tr>");
                swap=swap.substring(swap.indexOf(":")+1).trim();
                out.print("<tr><td>Swap</td><td>"+swap.substring(0,7)+"</td><td>"+swap.substring(8,16)+"</td><td>"+swap.substring(16)+"</td></tr>");
                cache=cache.substring(cache.indexOf(":")+1).trim();
                out.print("<tr><td>Cache</td><td>nulo</td><td>"+cache.substring(0,8)+"</td><td>"+cache.substring(9)+" </td></tr>");
                
          %>
      </table>
       <div id="cpuVal" style="display: none"></div>
        <div id="ramVal" style="display: none"></div>
      <table class="table-striped span4 table-condensed table-bordered">
          <tr><th style="background-color: gray">Processador</th><th style="background-color: gray"></th></tr>
          <% //Tabela Processador
            list.removeAll(list);
            ls_proc=Runtime.getRuntime().exec("cat /proc/cpuinfo");
            ls_in = new DataInputStream(ls_proc.getInputStream());
		while ((ls_str = ls_in.readLine()) != null) {
                    list.add(ls_str);
		  // System.out.println(ls_str);
		}
                String cpu=(String)list.get(4);
                cpu=cpu.substring(cpu.indexOf(":")+1);
                list.removeAll(list);
                ls_proc=Runtime.getRuntime().exec("lscpu");
                ls_in=new DataInputStream(ls_proc.getInputStream());
                while ((ls_str = ls_in.readLine()) != null) {
                    list.add(ls_str);
		}
                String cpus="0",thread="0",base=(String)list.get(7);
                for(int i=0;i<list.size();i++){
                    if(String.valueOf(list.get(i)).substring(0, 6).equals("CPU(s)")){
                        cpus=(String)list.get(i);
                        break;
                    }
                }
                for(int i=0;i<list.size();i++){
                    if(String.valueOf(list.get(i)).substring(0, 9).equals("Thread(s)")){
                        thread=(String)list.get(i);
                        break;
                    }
                }
                base=base.substring(base.indexOf(":")+1);
                cpus=cpus.substring(cpus.indexOf(":")+1);
                thread=thread.substring(thread.indexOf(":")+1);
                out.print(""+//<tr><td>Base</td><td>"+base+"</td></tr>
                        "<tr><td>Nome</td><td>"+cpu+"</td></tr>"+
                        "<tr><td>Núcleos</td><td>"+cpus+"</td></tr>"+
                        "<tr><td>Ações por núcleos (threads)</td><td>"+thread+"</td></tr>");
                
                ls_proc=Runtime.getRuntime().exec("ps aux");
                // get its output (your input) stream
                ls_in = new DataInputStream(ls_proc.getInputStream());
                int i=0;
                float load=0;
		while ((ls_str = ls_in.readLine()) != null) {
                   // list.add(ls_str);
                    if(i>0)
                        load+=Float.parseFloat(ls_str.substring(14,19).trim());
                    i++;
		}
                out.print("<tr><td>Uso atual da CPU</td><td>"+Math.round(load/Integer.parseInt(cpus.trim())) +"%</td></tr>");
                thread=cpus.trim();
          %>
      </table>
      
        <!--  <form action="Writer" method="POST" class="span2">
              <input type="submit" class="btn btn-info" value="Escreva" />
          </form>
          <form action="ControllerAsterisk?action=liga" method="POST" class="span2">
              <input type="submit" class="btn btn-info " value="Ligando" />
          </form>
          <form action="ControllerAsterisk?action=version" method="POST" class="span2">
              <input type="submit" class="btn btn-info" value="Versão" />
          </form>   -->
        </div> <!-- details -->
        <div class="clearfix"></div>
        <a style="padding-left: 3%" href="#" onclick="details();">+Detalhes</a>
      </div>
      <script>
          cpu=10;
          loadInternal("WebServ?cpuInfo=<%=thread %>",'cpuVal');
          ram=10;
          swap=10;
          loadInternal("WebServ?ramInfo=check",'ramVal');
            time=<%= session.getMaxInactiveInterval() %>;
            user="<%= session.getAttribute("user") %>";
            function details(){
                $('#details').toggle('slow');
            }
            $('#execBkp').click(function(){
                loadInternal("WebServ?execBkp=bkp",'bkp');
            });
            function startCountDown(){
                if(time>0){
                    $("#cron").html("Olá "+user+"</br>"+countDown(time));
                    time-=1;
                    setTimeout("startCountDown();", 1000);
                }else{
                    $("#cron").html("Sessão Expirada!");
                }
            }
            $(function(){
                startCountDown();
                $('#details').hide();
              //================================================================
              $('#hw').highcharts({
            chart: {
                type: 'bar'
            },
            title: {
                text: ''
            },
            xAxis: {
                categories: ['HD (GB)']
            },
            yAxis: {
                min: 0,
                title: {
                    text: ''
                }
            },
            legend: {
                backgroundColor: 'transparent',
                reversed: true
            },
            plotOptions: {
                series: {
                    stacking: 'normal'
                }
            },
                series: [{
                name: 'Livre',
                data: [<%=Math.round(Float.parseFloat(totalHD)-Float.parseFloat(usedHD)) %>]
            }, {
                name: 'Usado',
                data: [<%=usedHD %>]
            }]
        });
               //================================================================
                             //  cpu();
              $('#cpu').highcharts({
	
	    chart: {
	        type: 'gauge',
	        plotBackgroundColor: 'whitesmoke',
	        plotBackgroundImage: null,
	        plotBorderWidth: 0,
	        plotShadow: false
	    },
	    
	    title: {
	        text: ''
	    },
	    
	    pane: {
	        startAngle: -150,
	        endAngle: 150,
	        background: [{
	            backgroundColor: {
	                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
	                stops: [
	                    [0, '#FFF'],
	                    [1, '#333']
	                ]
	            },
	            borderWidth: 0,
	            outerRadius: '109%'
	        }, {
	            backgroundColor: {
	                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
	                stops: [
	                    [0, '#333'],
	                    [1, '#FFF']
	                ]
	            },
	            borderWidth: 1,
	            outerRadius: '107%'
	        }, {
	            // default background
	        }, {
	            backgroundColor: null,
	            borderWidth: 0,
	            outerRadius: '105%',
	            innerRadius: '103%'
	        }]
	    },
	       
	    // the value axis
	    yAxis: {
	        min: 0,
	        max: 100,
	        
	        minorTickInterval: 'auto',
	        minorTickWidth: 1,
	        minorTickLength: 10,
	        minorTickPosition: 'inside',
	        minorTickColor: '#666',
	
	        tickPixelInterval: 30,
	        tickWidth: 2,
	        tickPosition: 'inside',
	        tickLength: 10,
	        tickColor: '#666',
	        labels: {
	            step: 2,
	            rotation: 'auto'
	        },
	        title: {
	            text: 'CPU'
	        },
	        plotBands: [{
	            from: 0,
	            to: 60,
	            color: '#55BF3B' // green
	        }, {
	            from: 60,
	            to: 80,
	            color: '#DDDF0D' // yellow
	        }, {
	            from: 80,
	            to: 100,
	            color: '#DF5353' // red
	        }]        
	    },
	
	    series: [{
	        name: 'Uso',
	        data: [0],
	        tooltip: {
	            valueSuffix: '%'
	        }
	    }]
	
	}, 
	// Add some life
	function (chart) {
		if (!chart.renderer.forExport) {
		    setInterval(function () {
                        loadInternal("WebServ?cpuInfo=<%=thread %>",'cpuVal');
		        var point = chart.series[0].points[0],
		            newVal,
		            inc =1;
		        
		        newVal = point.y+cpu;
		        if (newVal < 0 || newVal > 100) {
		            newVal = point.y - inc-cpu;
		        }
		        //cpu=cpu+5;
                        //alert(cpu);
		        point.update(cpu);
		        
		    }, 3000);
		}
	});
        //===================================
        $('#ram').highcharts({
	
	    chart: {
	        type: 'gauge',
	        plotBackgroundColor: 'transparent',
	        plotBackgroundImage: null,
	        plotBorderWidth: 0,
	        plotShadow: false
	    },
	    
	    title: {
	        text: ''
	    },
	    
	    pane: {
	        startAngle: -150,
	        endAngle: 150,
	        background: [{
	            backgroundColor: {
	                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
	                stops: [
	                    [0, '#FFF'],
	                    [1, '#333']
	                ]
	            },
	            borderWidth: 0,
	            outerRadius: '109%'
	        }, {
	            backgroundColor: {
	                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
	                stops: [
	                    [0, '#333'],
	                    [1, '#FFF']
	                ]
	            },
	            borderWidth: 1,
	            outerRadius: '107%'
	        }, {
	            // default background
	        }, {
	            backgroundColor: '#DDD',
	            borderWidth: 0,
	            outerRadius: '105%',
	            innerRadius: '103%'
	        }]
	    },
	       
	    // the value axis
	    yAxis: {
	        min: 0,
	        max: 100,
	        
	        minorTickInterval: 'auto',
	        minorTickWidth: 1,
	        minorTickLength: 10,
	        minorTickPosition: 'inside',
	        minorTickColor: '#666',
	
	        tickPixelInterval: 30,
	        tickWidth: 2,
	        tickPosition: 'inside',
	        tickLength: 10,
	        tickColor: '#666',
	        labels: {
	            step: 2,
	            rotation: 'auto'
	        },
	        title: {
	            text: 'RAM'
	        },
	        plotBands: [{
	            from: 0,
	            to: 120,
	            color: '#55BF3B' // green
	        }, {
	            from: 60,
	            to: 80,
	            color: '#DDDF0D' // yellow
	        }, {
	            from: 80,
	            to: 100,
	            color: '#DF5353' // red
	        }]        
	    },
	
	    series: [{
	        name: 'Uso',
	        data: [0],
	        tooltip: {
	            valueSuffix: '%'
	        }
	    }]
	
	}, 
	// Add some life
	function (chart) {
		if (!chart.renderer.forExport) {
		    setInterval(function () {
                        loadInternal("WebServ?ramInfo=check",'ramVal');
		        var point = chart.series[0].points[0],
		            newVal,
		            inc = Math.round((Math.random() - 0.5) * 20);
		        
		        newVal = point.y + inc;
		        if (newVal < 0 || newVal > 100) {
		            newVal = point.y - inc;
		        }
		        
		        point.update(ram);
		       // alert(inc);
		    }, 3000);
		}
	});
        //===================================================
        $('#swap').highcharts({
	
	    chart: {
	        type: 'gauge',
	        plotBackgroundColor: null,
	        plotBackgroundImage: null,
	        plotBorderWidth: 0,
	        plotShadow: false
	    },
	    
	    title: {
	        text: ''
	    },
	    
	    pane: {
	        startAngle: -150,
	        endAngle: 150,
	        background: [{
	            backgroundColor: {
	                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
	                stops: [
	                    [0, '#FFF'],
	                    [1, '#333']
	                ]
	            },
	            borderWidth: 0,
	            outerRadius: '109%'
	        }, {
	            backgroundColor: {
	                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
	                stops: [
	                    [0, '#333'],
	                    [1, '#FFF']
	                ]
	            },
	            borderWidth: 1,
	            outerRadius: '107%'
	        }, {
	            // default background
	        }, {
	            backgroundColor: '#DDD',
	            borderWidth: 0,
	            outerRadius: '105%',
	            innerRadius: '103%'
	        }]
	    },
	       
	    // the value axis
	    yAxis: {
	        min: 0,
	        max: 100,
	        
	        minorTickInterval: 'auto',
	        minorTickWidth: 1,
	        minorTickLength: 10,
	        minorTickPosition: 'inside',
	        minorTickColor: '#666',
	
	        tickPixelInterval: 30,
	        tickWidth: 2,
	        tickPosition: 'inside',
	        tickLength: 10,
	        tickColor: '#666',
	        labels: {
	            step: 2,
	            rotation: 'auto'
	        },
	        title: {
	            text: 'SWP'
	        },
	        plotBands: [{
	            from: 0,
	            to: 120,
	            color: '#55BF3B' // green
	        }, {
	            from: 60,
	            to: 80,
	            color: '#DDDF0D' // yellow
	        }, {
	            from: 80,
	            to: 100,
	            color: '#DF5353' // red
	        }]        
	    },
	
	    series: [{
	        name: 'SWAP',
	        data: [0],
	        tooltip: {
	            valueSuffix: '%'
	        }
	    }]
	
	}, 
	// Add some life
	function (chart) {
		if (!chart.renderer.forExport) {
		    setInterval(function () {
		        var point = chart.series[0].points[0],
		            newVal,
		            inc = Math.round((Math.random() - 0.5) * 20);
		        
		        newVal = point.y + inc;
		        if (newVal < 0 || newVal > 100) {
		            newVal = point.y - inc;
		        }
		        
		        point.update(swap);
		        
		    }, 3000);
		}
	});
            });
            
        </script>        
    </body>
</html>
