/**
 * 
 */
package net.escritoriodigital.unicamp.redefor.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import net.escritoriodigital.unicamp.redefor.model.secure.Role;

/**
 * @author andrefabbro
 * 
 */
@FacesConverter("rolesConverter")
public class RolesConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent cmp, String value) {
		String[] str = value.split("[\\|]");
		return new Role(Long.valueOf(str[0]), str[1], str[2]);
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent cmp, Object object) {
		if (object == null)
			return null;
		if (!(object instanceof Role))
			return null;
		Role role = (Role) object;
		return role.getId() + "|" + role.getName() + "|"
				+ role.getDescription();
	}
}
