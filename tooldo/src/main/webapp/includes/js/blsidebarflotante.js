var isExtended = 1;

function blsidebarflt_1(){

	new Effect.toggle('blsidebarflt_main', 'blind', {scaleX: 'true', scaleY: 'true;', scaleContent: false});
	
	if(isExtended==0){

		
		$('blsidebarflt_lat').childNodes[0].src = $('blsidebarflt_lat').childNodes[0].src.replace(/(\.[^.]+)$/, '-active$1');
		
		new Effect.Fade('blsidebarflt_main',
   	{ duration:1.0, from:0.0, to:1.0 });
		
		isExtended++;
	}
	else{
		$('blsidebarflt_lat').childNodes[0].src = $('blsidebarflt_lat').childNodes[0].src.replace(/-active(\.[^.]+)$/, '$1');
		
		new Effect.Fade('blsidebarflt_main',
   	{ duration:1.0, from:1.0, to:0.0 });
		
		isExtended=0;
	}
	
}

function init(){
	Event.observe('blsidebarflt_lat', 'click', blsidebarflt_1, true);
}

Event.observe(window, 'load', init, true);