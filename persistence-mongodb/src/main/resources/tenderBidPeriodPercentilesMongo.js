function(year) {

	
	db.loadServerScripts();
	
   if(year!=undefined) { 
		var intYear=year.replace(/'/g, ""); 	
		var startStartDate=new Date(intYear,1,1);
		var endStartDate=new Date(intYear,12,31);
	   match=  { "$gte" : startStartDate , "$lte" : endStartDate }  ;
   
   }
   else 
	  match =  { "$ne" : null};
 

   
	var agg = db.release.aggregate(
	[
	{ "$match" :
	{ 
	    "tender.tenderPeriod.startDate" : { "$ne" : null},
	    "tender.tenderPeriod.endDate" : { "$ne" : null},	    
	    "tender.tenderPeriod.startDate" : match
	    }
	} ,
	
	{
	"$project" : {
	    "_id": false,
	        "tenderLengthDays" : {
	        $let: {
	            vars: {
	                endDate: "$tender.tenderPeriod.endDate",
	                startDate: "$tender.tenderPeriod.startDate"
	                },
	                in:  round ({  $divide : [ {$subtract: [   "$$endDate" ,  "$$startDate"  ]} , 86400000 ] },0)
	             }
	        }
	    }
	},
	{$sort: { tenderLengthDays : 1 }}
	]
	);
	
	var array=agg.toArray();	
	
	var len=array.length;
	
	result= { 
	    min: len>0 ? array[0]: 0, 
	    q1: len >0 ? array[Math.floor(len*.25)-1] : 0, 
	    median: len>0 ? array[Math.floor(len*.5)-1]:0 ,
	    q3: len>0 ? array[Math.floor(len*.75)-1]:0 ,
	    max:  len>0 ? array[len-1] : 0
	};

	return result;
}