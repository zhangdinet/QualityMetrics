var chart;
$(function (){
	var options={
        chart:{
            renderTo:'container',
            type:'column'
		},
         title: {
                text: '龙虎榜',
                style:{
            		fontWeight:'bold',
            		width:'200px',
            		marginLeft:'auto',
            		marginRight:'auto'
                }
            },
         credits: { //右下角网址信息
	            enabled: false
	        },
         xAxis: {
            	title:{
            		text:'产品模块名称',
                	style:{
                		fontWeight:'bold',
                		fontSize:'16px',
                		width:'200px',
                		marginLeft:'auto',
                		marginRight:'auto'
                	}
            	},
            	 categories: [],
                 labels: {
                     rotation: 60
                 }
            },
         yAxis: {
        	 	max:5,
                title: {
                    text: '总平均分',
                    style:{
                		fontWeight:'bold',
                		fontSize:'16px',
                	}
                }
            },
          legend:{
                enabled:false
            },
            plotOptions: {
	        	 column: {
	        		pointPadding: 0.2,
		            borderWidth: 0,
	            	dataLabels: {
	            	   enabled: true,
	            	   y:40,
	            	  backgroundColor: 'rgba(252, 255, 197, 0.7)',
	            	   borderRadius: 5,
	            	   borderWidth: 1,
	                   borderColor: '#AAA',
	            	   formatter:function()
	            	   {
	            			if(this.y!=0)
	            			{
	            				return this.y;
	            			}
	            	   }
	            	}
	        	 }
	            },
         tooltip: {
                formatter:function(){
            	return ' <b>'+ this.x +
                '</b><br/><b>'+ this.y +'</b>';
            }
            },
         series: [{
                data: []
            }]
        };
        chart = new Highcharts.Chart(options);
    });