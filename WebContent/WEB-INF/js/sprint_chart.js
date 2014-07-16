var chart;
$(function (){
	var options={
	        chart:{
	            renderTo:'container',
	            type:'line'
					},
	         title: {
	                text: 'Sprint质量衡量分数',
	                x: -20 //center
	            },
	         subtitle: {
	                x: -20
	            },
	         credits: { //右下角网址信息
		            enabled: false
		        },
	         xAxis: {
	            	title:{
	            		text:'Sprint',
	                	style:{
	                		fontWeight:'bold',
	                		fontSize:'16px',
	                		width:'200px',
	                		marginLeft:'auto',
	                		marginRight:'auto'
	                	}
	            	}
	            },
	         yAxis: {
	        	 	max:5,
	        	 	min:0,
	                title: {
	                    text: '分数',
	                    style:{
	                		fontWeight:'bold',
	                		fontSize:'16px',
            			}
	                },
	                plotLines: [{
	                    value: 0,
	                    width: 1,
	                    color: '#808080'
	                }]
	            },
	          legend:{
	                enabled:false
	            },
	           
	         tooltip: {
                formatter:function(){
                	return 'sprint: <b>'+ this.x +
                	'</b><br/>score: <b>'+ this.y +'</b>';
                }
	         },
	            
	         plotOptions: {
	        	 column: {
	        		 pointPadding: 0.2,
	            	borderWidth: 0,
	            	dataLabels: {
	            		enabled: true,
	            		formatter:function(){
	            			return this.y;
	            		}
	            	}
	        	 }
	            },
	         series: [{
	                data: []
	            }]
	        };
	        chart = new Highcharts.Chart(options);
    });