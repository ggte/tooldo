SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `UNICAMP_REDEFOR`
--

--
-- Dumping data for table `rfr_user`
--

INSERT INTO `UNICAMP_REDEFOR`.`RFR_USER` (`ID`, `EMAIL`, `NAME`, `PHONENUMBER`, `USERNAME`, `PASSWORD`, `CREATED`, `ENABLED`, `FK_RFR_PROFILE`) VALUES
(10, 'felipe@escritoriodigital.net', 'Revisor de Produ�ao', '78104391', 'revisor.producao', '202cb962ac59075b964b07152d234b70', '2010-07-14', '1', 6),
(11, 'felipe@escritoriodigital.net', 'Revisor de Navega��o', '78104391', 'revisor.navegacao', '202cb962ac59075b964b07152d234b70', '2010-07-14', '1', 8),
(14, 'felipe@escritoriodigital.net', 'Professor Coordenador', '78104391', 'professor7', '202cb962ac59075b964b07152d234b70', '2010-07-14', '1', 3),
(15, 'felipe@escritoriodigital.com.br', 'Felipe Alexandre', '19 912239499', 'felipe.alexandre', '202cb962ac59075b964b07152d234b70', '2010-07-16', '1', 2),
(16, 'roxane.rojo@unicamp.br', 'Roxane Rojo', '+551935216664', 'roxane.rojo', '8a43753368c298153275f3e7f019bdd2', '2010-07-23', '1', 2);

--
-- Dumping data for table `rfr_workflow_entity`
--

INSERT INTO `UNICAMP_REDEFOR`.`RFR_WORKFLOW_ENTITY` (`ID`, `NAME`, `DESCRIPTION`) VALUES
(1, 'Workflow Tema', 'Processo de publica��o doTema');

--
-- Dumping data for table `rfr_workflow_phase_entity`
--

INSERT INTO `UNICAMP_REDEFOR`.`RFR_WORKFLOW_PHASE_ENTITY` (`ID`, `NAME`, `DESCRIPTION`, `FINISHED`, `PHASE_ORDER`, `FK_RFR_WORKFLOW_ENTITY`) VALUES
(1, 'Professor', 'Novo tema', '1', 1, 1),
(2, 'Pr�-Produ��o', 'Aprova��o da Pr�-produ��o', '1', 2, 1),
(3, 'Revis�o e Produ��o', 'Revis�o de Ortogr�fia e Objetos e Produ��o de Objetos', '1', 3, 1),
(4, 'Revis�o de Navega��o', 'Revis�o de Navega��o', '1', 4, 1),
(5, 'Revis�o do Professor', 'Revis�o do Professor', '1', 5, 1),
(6, 'Publica��o', 'Publica��o', '0', 6, 1);

--
-- Dumping data for table `rfr_workflow_stage_entity`
--

INSERT INTO `UNICAMP_REDEFOR`.`RFR_WORKFLOW_STAGE_ENTITY` (`ID`, `NAME`, `DESCRIPTION`, `FINISHED`, `REQUIRED`, `FK_RFR_USER`, `FK_RFR_WORKFLOW_PHASE_ENTITY`, `FK_RFR_PROFILE`) VALUES
(1, 'Professor', 'Novo tema', '1', '1', 3, 1, 2),
(2, 'Pr�-Produ��o', 'Aprova��o da Pr�-produ��o', '1', '1', 4, 2, 4),
(3, 'Revis�o de Ortogr�fia', 'Revis�o de Ortogr�fia', '1', '1', 9, 3, 5),
(4, 'Revis�o de Objetos', 'Revis�o de Objetos', '1', '1', 5, 3, 7),
(5, 'Produ��o de Objetos', 'Produ��o de Objetos', '1', '1', 10, 3, 6),
(6, 'Revis�o de Navega��o', 'Revis�o de Navega��o', '1', '1', 11, 4, 8),
(7, 'Revis�o do Professor', 'Revis�o do Professor', '1', '1', 3, 5, 2),
(8, 'Publica��o', 'Publica��o', '0', '1', NULL, 6, 3);

--
-- Dumping data for table `rfr_discipline`
--

INSERT INTO `UNICAMP_REDEFOR`.`RFR_DISCIPLINE` (`ID`, `NAME`, `DESCRIPTION`, `CREATED`, `ENABLED`, `FK_RFR_COURSE`, `FK_RFR_USER`) VALUES
(1, 'Disciplina 1', 'Descri��o da Disciplina 1', '2010-07-08', '1', 1, 3),
(2, 'C�lculo 1', '', '2010-07-16', '1', 2, 15),
(3, 'Estat�stica', '', '2010-07-16', '1', 2, 15),
(4, 'C�lculo 2', '', '2010-07-16', '1', 2, 3),
(5, 'Hist�ria da Disciplina de L�ngua Portuguesa no Brasil', 'Hist�ria da Disciplina de L�ngua Portuguesa no Brasil', '2010-07-23', '1', 3, 16);

--
-- Dumping data for table `rfr_theme`
--

INSERT INTO `UNICAMP_REDEFOR`.`RFR_THEME` (`ID`, `NAME`, `DESCRIPTION`, `CREATED`, `ENABLED`, `PUBLISHED`, `FK_RFR_DISCIPLINE`, `FK_RFR_USER`, `FK_RFR_USER_LOCK`, `FK_RFR_WORKFLOW_ENTITY`) VALUES
(1, 'Tema Disciplina 1', 'Descri��o do Tema', '2010-07-14', '1', 0, 1, 3, NULL, 1),
(2, 'Limites', '', '2010-07-16', '1', 0, 2, 15, NULL, NULL),
(4, 'De fins do s�c. XIX a 1960', 'De fins do s�c. XIX a 1960', '2010-07-23', '1', 0, 5, 16, NULL, NULL);

--
-- Dumping data for table `rfr_topic`
--

INSERT INTO `UNICAMP_REDEFOR`.`RFR_TOPIC` (`ID`, `NAME`, `DESCRIPTION`, `CREATED`, `ENABLED`, `FK_RFR_USER_LOCK`, `FK_RFR_THEME`, `FK_RFR_USER`) VALUES
(1, 'T�pico 1', 'T�pico 1', '2010-07-14', '1', NULL, 1, 3),
(2, 'Fun��es Continuas', '', '2010-07-16', '1', NULL, 2, 15),
(3, 'Fun��es Descontinuas', '', '2010-07-16', '1', NULL, 2, 15),
(4, 'Contexto hist�rico e educacional', 'Contexto hist�rico e educacional', '2010-07-23', '1', NULL, 4, 16);

--
-- Dumping data for table `rfr_granted_users_discipline`
--

INSERT INTO `UNICAMP_REDEFOR`.`RFR_GRANTED_USERS_DISCIPLINE` (`ID`, `FK_RFR_USER`, `FK_RFR_DISCIPLINE`) VALUES
(3, 4, 1),
(4, 5, 1),
(5, 7, 1),
(7, 8, 1),
(11, 9, 1),
(12, 10, 1),
(13, 11, 1),
(14, 14, 1);

--
-- Dumping data for table `rfr_page`
--

INSERT INTO `UNICAMP_REDEFOR`.`RFR_PAGE` (`ID`, `NAME`, `DESCRIPTION`, `CREATED`, `POSITION`, `ENABLED`, `FK_RFR_TOPIC`, `FK_RFR_USER_LOCK`, `FK_RFR_USER`) VALUES
(7, 'P�gina 7', 'P�gina 7', '2010-07-15', 1, '1', 1, NULL, 3),
(8, 'Introdu��o', '', '2010-07-16', 1, '1', 2, NULL, 15),
(9, 'Gr�fico da Fun��o', '', '2010-07-16', 2, '1', 2, NULL, 15),
(10, 'Teste', '', '2010-07-16', 3, '1', 2, NULL, 15),
(11, 'Introdu��o', '', '2010-07-16', 1, '1', 3, NULL, 15),
(12, 'Ponto de partida', 'Ponto de partida', '2010-07-23', 1, '1', 4, NULL, 16),
(13, 'Ponto de partida 2', 'Ponto de partida 2', '2010-07-23', 2, '1', 4, NULL, 16),
(14, 'Ponto de partida 3', 'Ponto de partida 3', '2010-07-23', 3, '1', 4, NULL, 16),
(15, 'Ponto de partida 4', 'Ponto de partida 4', '2010-07-23', 4, '1', 4, NULL, 16);

--
-- Dumping data for table `rfr_roadmap_content`
--

INSERT INTO `UNICAMP_REDEFOR`.`RFR_ROADMAP_CONTENT` (`ID`, `ROADMAP`, `FILE`) VALUES
(2, NULL, NULL),
(3, NULL, NULL);

--
-- Dumping data for table `rfr_content`
--

INSERT INTO `UNICAMP_REDEFOR`.`RFR_CONTENT` (`ID`, `CONTENT`, `CREATED`, `FK_RFR_PAGE`, `FK_RFR_TYPE_CONTENT`, `FK_RFR_ROADMAP_CONTENT`) VALUES
(2, '', '2010-07-22', 9, 4, 2),
(3, '<p>\r\n&nbsp;\r\n</p>\r\n<p>\r\n&nbsp;\r\n</p>\r\n<p>\r\n&nbsp;\r\n</p>\r\n<p>\r\n&nbsp;\r\n</p>\r\n<p>\r\n&nbsp;\r\n</p>\r\n<table border="0">\r\n	<tbody>\r\n		<tr>\r\n			<td>O s&eacute;c. XIX foi palco de muitas mudan&ccedil;as na sociedade brasileira, em especial nas grandes prov&iacute;ncias (Rio de Janeiro, S&atilde;o Paulo, Minas Gerais): a proclama&ccedil;&atilde;o da independ&ecirc;ncia da col&ocirc;nia; a implanta&ccedil;&atilde;o da Rep&uacute;blica e o fim da Monarquia; a promulga&ccedil;&atilde;o da primeira Constitui&ccedil;&atilde;o de 1824 e sua revis&atilde;o republicana em 1891; o fim do trabalho escravo e sua paulatina substitui&ccedil;&atilde;o pelo trabalho assalariado; a moderniza&ccedil;&atilde;o do campo, o crescimento das cidades e sua industrializa&ccedil;&atilde;o.<br />\r\n			Tanto a burguesia como os novos trabalhadores aflu&iacute;am &agrave;s cidades das grandes prov&iacute;ncias, buscando vida mais confort&aacute;vel e educa&ccedil;&atilde;o.<br />\r\n			No entanto, a educa&ccedil;&atilde;o p&uacute;blica ainda era pouco desenvolvida e enfatizada em fins do s&eacute;culo XIX. Por exemplo, em 1890, apenas 15% dos brasileiros eram alfabetizados.<br />\r\n			Faltavam homens diplomados para o servi&ccedil;o p&uacute;blico de alto escal&atilde;o, para o clero e profissionais liberais. Fora os Semin&aacute;rios, o Brasil como um todo tinha, no s&eacute;c. XIX, apenas 10 escolas superiores, entre Faculdades, cursos militares e escolas. Para o ingresso nas escolas superiores, n&atilde;o era necess&aacute;rio o secund&aacute;rio, mas passar nos Exames Preparat&oacute;rios.&nbsp;&nbsp;</td>\r\n			<td>&nbsp; \r\n			<p align="center">\r\n			<img src="http://www.unicamp.br/iel/memoria/Ensaios/LiteraturaInfantil/crian%E7as%20na%20escola.gif" alt="" width="453" height="332" align="textTop" /><br />\r\n			Bairro do Bixiga, S&atilde;o Paulo, em imagem de 1862: moradia dos imigrantes italianos e dos ex-escravos. \r\n			</p>\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td>&nbsp;</td>\r\n			<td>&nbsp;</td>\r\n		</tr>\r\n	</tbody>\r\n</table>\r\n', '2010-07-23', 12, 3, 3);

--
-- Dumping data for table `rfr_question_content`
--

INSERT INTO `UNICAMP_REDEFOR`.`RFR_QUESTION_CONTENT` (`ID`, `QUESTION`, `FK_RFR_CONTENT`) VALUES
(2, 'Qual � a cor do meu cabelo?', 2);

--
-- Dumping data for table `rfr_alternative_question`
--

INSERT INTO `UNICAMP_REDEFOR`.`RFR_ALTERNATIVE_QUESTION` (`ID`, `ALTERNATIVE`, `CORRECT`, `FK_RFR_QUESTION_CONTENT`) VALUES
(3, 'Alternativa 2', '1', 2),
(4, 'Alternativa 1', '0', 2),
(5, 'Alternativa 3', '1', 2);

--
-- Dumping data for table `rfr_history_workflow_theme`
--

INSERT INTO `UNICAMP_REDEFOR`.`RFR_HISTORY_WORKFLOW_THEME` (`ID`, `CREATED`, `COMMENTS`, `FK_RFR_THEME`, `ACCEPT`, `FK_RFR_WORKFLOW_STAGE`, `FK_RFR_USER`) VALUES
(1, '2010-07-21', '', 1, '1', 2, 4),
(2, '2010-07-21', '', 1, '1', 3, 9),
(3, '2010-07-21', '', 1, '1', 4, 5),
(4, '2010-07-21', '', 1, '1', 5, 10),
(5, '2010-07-21', 'teste', 1, '1', 6, 11),
(6, '2010-07-21', 'teste', 1, '1', 6, 11),
(7, '2010-07-21', 'teste123', 1, '1', 6, 11),
(8, '2010-07-21', 'teste123', 1, '1', 6, 11),
(9, '2010-07-21', 'teste123', 1, '1', 6, 11),
(10, '2010-07-21', 'dsaiojdsai ajid iasdjiasdjiio ji as', 1, '1', 6, 11),
(11, '2010-07-21', 'asdfghyujjj', 1, '1', 6, 11),
(12, '2010-07-21', 'asdfghyujjj', 1, '1', 6, 11),
(13, '2010-07-21', 'asdfghyujjj', 1, '1', 6, 11),
(14, '2010-07-21', 'asdfghyujjj', 1, '1', 6, 11),
(15, '2010-07-21', 'asdfghyujjj', 1, '1', 6, 11),
(16, '2010-07-21', 'asdfghyujjj', 1, '1', 6, 11),
(17, '2010-07-21', 'asdfghyujjj', 1, '1', 6, 11),
(18, '2010-07-21', '123', 1, '1', 6, 11),
(19, '2010-07-21', '123', 1, '1', 6, 11),
(20, '2010-07-22', 'te', 1, '1', 6, 11),
(21, '2010-07-22', 'j', 1, '1', 6, 11),
(22, '2010-07-22', 'fejaidjasiodasij', 1, '1', 6, 11),
(23, '2010-07-22', 'bla bla bla', 1, '1', 7, 3),
(24, '2010-07-22', 'bla bla bla', 1, '1', 7, 3);