package net.escritoriodigital.unicamp.redefor.model.theme;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.model.page.Page;
import net.escritoriodigital.unicamp.redefor.model.topic.Topic;
import net.escritoriodigital.unicamp.redefor.service.theme.ThemeService;

/**
 * @author andrefabbro
 *
 */
public class ThemeUtil {
	
	public static final void unlockTheme(ThemeService themeService, Theme theme) throws ServiceException {
		
		theme = themeService.getById(theme.getId());
		
		if (theme.getLock() != null) {	
			theme.setLock(null);
			for (Topic topic : theme.getTopics()) {
				topic.setLock(null);
				for (Page page : topic.getPages()) page.setLock(null);
			}
			
			themeService.save(theme);
		}
	}

}
