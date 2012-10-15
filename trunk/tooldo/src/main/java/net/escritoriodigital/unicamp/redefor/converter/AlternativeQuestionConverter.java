package net.escritoriodigital.unicamp.redefor.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import net.escritoriodigital.unicamp.redefor.model.content.AlternativeQuestion;

public class AlternativeQuestionConverter implements javax.faces.convert.Converter{

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		
		String [] str = value.split(":");

		if(str.length == 1) return new AlternativeQuestion(); 
		
		return new AlternativeQuestion(Long.valueOf(str[0]), str[1], Boolean.valueOf(str[2]));
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		AlternativeQuestion optionItem = (AlternativeQuestion) value;
		return optionItem.getId() + ":"  + optionItem.getAlternative() + ":" + optionItem.getCorrect();

	}

	
}
