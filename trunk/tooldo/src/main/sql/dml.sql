-- phpMyAdmin SQL Dump
-- version 3.3.3
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 26, 2010 at 04:59 PM
-- Server version: 5.1.47
-- PHP Version: 5.2.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `TOOLDO`
--

--
-- Dumping data for table `rfr_role`
--

INSERT INTO `RFR_ROLE` (`ID`, `NAME`, `DESCRIPTION`) VALUES
(1, 'ROLE_ADMINISTRATOR', 'Administrador'),
(2, 'ROLE_COURSE_VIEW', 'Curso: Visualizar'),
(3, 'ROLE_COURSE_MODIFY', 'Curso: Editar'),
(4, 'ROLE_DISCIPLINE_VIEW', 'Disciplina: Visualizar'),
(5, 'ROLE_DISCIPLINE_MODIFY', 'Disciplina: Editar'),
(6, 'ROLE_THEME_VIEW', 'Tema: Visualizar'),
(7, 'ROLE_THEME_MODIFY', 'Tema: Editar'),
(8, 'ROLE_TOPIC_VIEW', 'T�pico: Visualizar'),
(9, 'ROLE_TOPIC_MODIFY', 'T�pico: Editar'),
(10, 'ROLE_PAGE_VIEW', 'P�gina: Visualizar'),
(11, 'ROLE_PAGE_MODIFY', 'P�gina: Editar'),
(12, 'ROLE_WORKFLOW_ONLY', 'Somente Workflow'),
(13, 'ROLE_WORKFLOW_PUBLISH', 'Publicar'),
(14, 'ROLE_WORKFLOW_START', 'Liberar Tema'),
(15, 'ROLE_WORKFLOW_READJUST', 'Readequar'),
(16, 'ROLE_CONTENT_ROADMAP', 'Conte�do: Roadmap');

--
-- Dumping data for table `rfr_profile`
--

INSERT INTO `RFR_PROFILE` (`ID`, `NAME`, `FIXED`) VALUES
(1, 'Administrador', '1'),
(2, 'Professor Autor', '1'),
(3, 'Professor Coordenador', '1'),
(4, 'Pr�-produ��o', '1'),
(5, 'Revisor Ortogr�fico', '1'),
(6, 'Revisor de Produ��o', '1'),
(7, 'Revisor de Objeto', '1'),
(8, 'Revisor de Navega��o', '1'),
(10, 'Auxiliar de Professor', '1'),
(11, 'Produtor de Objetos', '1');

--
-- Dumping data for table `rfr_user`
--
INSERT INTO `RFR_USER` (`ID`, `EMAIL`, `NAME`, `PHONENUMBER`, `USERNAME`, `PASSWORD`, `CREATED`, `ENABLED`, `FK_RFR_PROFILE`) VALUES
(1,'andre.fabbro@escritoriodigital.net','Adminstrador','(19) 9305-6495','administrador','698dc19d489c4e4db73e28a713eab07b','2010-07-08','1',1),
(2,'profautor@teste.com','Professor Autor','','profautor','698dc19d489c4e4db73e28a713eab07b','2010-08-23','1',2),
(3,'preprod@teste.com','Pr� Produtor','','preprod','698dc19d489c4e4db73e28a713eab07b','2010-08-23','1',4),
(4,'revisort@teste.com','Revisor Ortogr�fico','','revisort','698dc19d489c4e4db73e28a713eab07b','2010-08-23','1',5),
(5,'prodobj@teste.com','Produtor de Objetos','','prodobj','698dc19d489c4e4db73e28a713eab07b','2010-08-23','1',11),
(6,'revisobj@teste.com','Revisor de Objetos','','revisobj','698dc19d489c4e4db73e28a713eab07b','2010-08-23','1',7),
(7,'revprod@teste.com','Revisor de Produ��o','','revprod','698dc19d489c4e4db73e28a713eab07b','2010-08-23','1',6),
(8,'revinav@teste.com','Revisor de Navega��o','','revinav','698dc19d489c4e4db73e28a713eab07b','2010-08-23','1',8),
(9,'profcoord@teste.com','Professor Coordenador','','profcoord','698dc19d489c4e4db73e28a713eab07b','2010-08-23','1',3);

--
-- Dumping data for table `rfr_granted_profile_roles`
--

INSERT INTO `RFR_GRANTED_PROFILE_ROLES` (`ID`, `FK_RFR_PROFILE`, `FK_RFR_ROLE`) VALUES
(1,1,1),(122,10,2),(123,10,4),(124,10,6),(125,10,7),(126,10,8),(127,10,9),(128,10,10),(129,10,11),(130,10,16),
(131,2,2),(132,2,4),(133,2,7),(134,2,9),(135,2,11),(136,2,14),(137,2,15),(138,2,16),(181,4,12),(182,4,15),
(183,4,6),(198,5,11),(199,5,12),(200,5,6),(201,5,8),(205,6,11),(206,6,12),(207,6,8),(208,6,6),(209,8,11),
(210,8,12),(211,8,6),(212,8,8),(213,11,16),(214,11,12),(215,11,11),(216,11,6),(217,11,8),(218,3,6),(219,3,13),
(220,3,15),(221,3,12),(222,7,11),(223,7,12),(224,7,6),(225,7,8);

--
-- Dumping data for table `rfr_workflow_template`
--

INSERT INTO `RFR_WORKFLOW_TEMPLATE` (`ID`, `NAME`, `DESCRIPTION`, `ENABLED`) VALUES
(1, 'Workflow Tema', 'Processo de publica��o do Tema', '1');

--
-- Dumping data for table `rfr_workflow_phase_template`
--

INSERT INTO `RFR_WORKFLOW_PHASE_TEMPLATE` (`ID`, `NAME`, `DESCRIPTION`, `PHASE_ORDER`, `FK_RFR_WORKFLOW_TEMPLATE`) VALUES
(1, 'Professor', 'Novo tema', 1, 1),
(2, 'Pr�-Produ��o', 'Aprova��o da Pr�-produ��o', 2, 1),
(3, 'Revis�o Ort. e Produ��o', 'Revis�o Ortogr�fia e Produ��o de Objetos', 3, 1),
(4, 'Revis�o de Objetos/Prod', 'Revis�o de Objetos e Revis�o de Produ��o', 4, 1),
(5, 'Revis�o de Navega��o', 'Revis�o de Navega��o', 5, 1),
(6, 'Revis�o do Professor', 'Revis�o do Professor', 6, 1),
(7, 'Publica��o', 'Publica��o', 7, 1);

--
-- Dumping data for table `rfr_workflow_stage_template`
--

INSERT INTO `RFR_WORKFLOW_STAGE_TEMPLATE` (`ID`, `NAME`, `DESCRIPTION`, `REQUIRED`, `FK_RFR_WORKFLOW_PHASE_TEMPLATE`, `FK_RFR_PROFILE`) VALUES
(1, 'Professor', 'Novo tema', '1', 1, 2),
(2, 'Pr�-Produ��o', 'Aprova��o da Pr�-produ��o', '1', 2, 4),
(3, 'Revis�o Ortogr�fica', 'Revis�o Ortogr�fia do Conte�do', '1', 3, 5),
(4, 'Produ��o dos Objetos', 'Produ��o dos Objetos do Conte�do', '1', 3, 11),
(5, 'Revis�o de Objetos', 'Revis�o de Objetos', '1', 4, 7),
(6, 'Revis�o de Produ��o', 'Revis�o da Produ��o', '1', 4, 6),
(7, 'Revis�o de Navega��o', 'Revis�o de Navega��o', '1', 5, 8),
(8, 'Revis�o do Professor', 'Revis�o do Professor Autor', '1', 6, 2),
(9, 'Publica��o', 'Publica��o do Tema pelo Coordenador', '1', 7, 3);

--
-- Dumping data for table `rfr_course`
--

INSERT INTO `RFR_COURSE` (`ID`, `NAME`, `DESCRIPTION`, `CREATED`, `ENABLED`) VALUES
(1, 'Educa��o F�sica', 'Curso de Educa��o F�sica', '2010-01-01', '1'),
(2, 'F�sica', 'Curso de F�sica', '2010-01-01', '1'),
(3, 'Hist�ria', 'Curso de Hist�ria', '2010-01-01', '1'),
(4, 'L�ngua Portuguesa', 'Curso de L�ngua Portuguesa', '2010-01-01', '1'),
(5, 'Matem�tica', 'Curso de Matem�tica', '2010-01-01', '1');

--
-- Dumping data for table `rfr_type_content`
--

INSERT INTO `RFR_TYPE_CONTENT` (`ID`, `NAME`, `TYPECONTENT`) VALUES
(1, 'Imagem a esquerda e texto a direita', '<img src="/" alt="Imagem" style="float: left; padding: 0 10px 10px 0" /> \r\n Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis feugiat mi at nisi dignissim sollicitudin. Quisque eleifend, libero at sagittis pretium, sapien risus rhoncus erat, ac porta ligula eros at leo. Duis dictum placerat metus, a porttitor ligula molestie sed. In dictum mauris eu nulla dignissim gravida. Nunc non est eu nulla suscipit rutrum. Nulla facilisi. Nulla facilisis, odio non vulputate faucibus, ipsum metus placerat mauris, eget aliquet enim urna non lacus. Suspendisse mattis rhoncus nunc, ac tristique nunc convallis vitae. Nam ut erat sem. Quisque iaculis faucibus ante commodo dictum. Donec ut dolor sapien.\r\n'),
(2, 'Imagem a direita e texto a esquerda', '<img src="/" alt="Imagem" style="float: right; padding: 0 0 10px 10px" /> \r\n Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis feugiat mi at nisi dignissim sollicitudin. Quisque eleifend, libero at sagittis pretium, sapien risus rhoncus erat, ac porta ligula eros at leo. Duis dictum placerat metus, a porttitor ligula molestie sed. In dictum mauris eu nulla dignissim gravida. Nunc non est eu nulla suscipit rutrum. Nulla facilisi. Nulla facilisis, odio non vulputate faucibus, ipsum metus placerat mauris, eget aliquet enim urna non lacus. Suspendisse mattis rhoncus nunc, ac tristique nunc convallis vitae. Nam ut erat sem. Quisque iaculis faucibus ante commodo dictum. Donec ut dolor sapien.\r\n'),
(3, 'Modelo para livre formata��o', ''),
(4, 'Modelo de Testes', '');

