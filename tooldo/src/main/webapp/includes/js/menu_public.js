function getElementsByClass(nomeClasse, node, tag) {
	var classElementos = new Array();
	if (node == null) {
		node = document;
	}
	if (tag == null) {
		tag = '*';
	}
	var els = node.getElementsByTagName(tag);
	var elsLen = els.length;
	var pattern = new RegExp("(^|\\s)" + nomeClasse + "(\\s|$)");
	for (i = 0, j = 0; i < elsLen; i++) {
		if (pattern.test(els[i].className)) {
			classElementos[j] = els[i];
			j++;
		}
	}
	return classElementos;
}

function setaLinks() {

	var mainMenus = getElementsByClass("mainMenu");
	for (i = 0; i < mainMenus.length; i++) {
		mainMenus[i].onclick = function() {
			var submenu = this.parentNode.getElementsByTagName("ul");

			if (submenu[0].style.display == "none"
					|| submenu[0].style.display == "") {
				zeraSubMenus();
				submenu[0].style.display = "block";
				return false;
			}
			if (submenu[0].style.display == "block") {
				zeraSubMenus();
				submenu[0].style.display = "none";
				return false;
			}
		}
	}
	var expMenus = getElementsByClass("ulselected");
	if(expMenus[0] != undefined) expMenus[0].style.display = "block";
}

function zeraSubMenus() {

	submenus = document.getElementById("blsidebar_flotante")
			.getElementsByTagName("ul");
	for (i = 1; i < submenus.length; i++) {
		submenus[i].style.display = "none";
	}
}