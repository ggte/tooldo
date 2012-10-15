package net.escritoriodigital.unicamp.redefor.bean.profile;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.model.secure.Profile;
import net.escritoriodigital.unicamp.redefor.model.secure.Role;
import net.escritoriodigital.unicamp.redefor.service.secure.ProfileService;
import net.escritoriodigital.unicamp.redefor.service.secure.RoleService;
import net.escritoriodigital.unicamp.redefor.utils.jsf.FacesUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
@Qualifier("profileBean")
public class ProfileBean {
	
	private static final String SUCCESS = "profileSuccess";

	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private RoleService roleService;

	private Profile profile = new Profile();

	private List<Profile> profiles;
	
	private List<Role> roles = new ArrayList<Role>();

	public String list() throws Exception {
		this.profiles = (List<Profile>) profileService.findAll();
		this.clear();
		return SUCCESS;
	}
	
	public void edit() throws Exception {
		if (profile != null) profile = profileService.getById(profile.getId());
		this.listRolesAvailable();
	}
	
	public void clear() throws Exception {
		this.profile = new Profile();
		this.listRolesAvailable();
	}

	public void listRolesAvailable() throws Exception {
		roles = (List<Role>) roleService.findAll();
	}

	public String save() throws Exception {
		
		System.out.println("HEllo World!");
		
		try {
			if(profile.getFixed() == null) profile.setFixed(false);
			profileService.save(profile);
		} catch (ServiceException e) {
			FacesUtils.addErrorMessage(e.getMessage());
		}
		
		return list();
	}

	public String remove() throws Exception {
		if (profile != null) {
			try {
				profileService.delete(profile.getId());
				FacesUtils.addInfoMessage(" Perfil excluído com sucesso.");
			} catch (ServiceException e) {
				FacesUtils.addErrorMessage(" Erro ao excluir o Perfil " + profile.getName());
			}
		} else {
			FacesUtils.addErrorMessage(" Não foi possível localizar o perfil.");
		}
		
		return list();
	}

	
	public Profile getProfile() {
		return profile;
	}
	
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	public List<Profile> getProfiles() {
		return profiles;
	}
	
	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
