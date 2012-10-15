package net.escritoriodigital.unicamp.redefor.service.content;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.model.content.TypeContent;
import net.escritoriodigital.unicamp.redefor.service.AbstractToolDoServiceTests;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration(defaultRollback = true)
@Transactional
public class TypeContentTest extends AbstractToolDoServiceTests {

	@Autowired
	private TypeContentService service;



	@Test
	public void testInsertTypeContent() {


		try {

			TypeContent entity = new TypeContent();
			entity.setName("Imagem a esquerda e texto a direita");
			entity.setTypeContent("<table width='100%' border='1'><tr><td><img src='blank.jpg' width='150' height='150' /></td><td>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras eget leo in nisi commodo malesuada vitae sit amet diam. Maecenas nunc ipsum; molestie sed sagittis non, dictum sit amet lectus. Donec eu dui massa, a gravida risus.</td></tr></table>");
			service.save(entity);

			TypeContent entity2 = new TypeContent();
			entity2.setName("Imagem a direita e texto a esquerda");
			entity2.setTypeContent("<table width='100%' border='1'><tr><td>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras eget leo in nisi commodo malesuada vitae sit amet diam. Maecenas nunc ipsum; molestie sed sagittis non, dictum sit amet lectus. Donec eu dui massa, a gravida risus.</td><td><img src='blank.jpg' width='150' height='150' /></td></tr></table>");
			service.save(entity2);

			TypeContent entity3 = new TypeContent();
			entity3.setName("Modelo para livre formatação");
			entity3.setTypeContent("");
			service.save(entity3);

			TypeContent entity4 = new TypeContent();
			entity4.setName("Modelo de testes");
			entity4.setTypeContent("");
			service.save(entity4);

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
