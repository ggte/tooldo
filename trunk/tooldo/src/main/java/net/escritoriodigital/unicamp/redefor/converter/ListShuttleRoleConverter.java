package net.escritoriodigital.unicamp.redefor.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.escritoriodigital.unicamp.redefor.model.secure.Role;

public class ListShuttleRoleConverter implements javax.faces.convert.Converter{

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		
		String [] str = value.split(":");

		return new Role(Long.valueOf(str[0]), str[1], str[2]);
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		Role optionItem = (Role) value;
		return optionItem.getId() + ":"  + optionItem.getName() + ":" + optionItem.getDescription();

	}

	
}
