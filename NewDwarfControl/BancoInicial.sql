-- --------------------------------------------------------
-- Servidor:                     127.0.0.1
-- Versão do servidor:           10.2.9-MariaDB - mariadb.org binary distribution
-- OS do Servidor:               Win64
-- HeidiSQL Versão:              9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Copiando estrutura do banco de dados para new_dwarf_control
CREATE DATABASE IF NOT EXISTS `new_dwarf_control` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `new_dwarf_control`;

-- Copiando estrutura para tabela new_dwarf_control.tb_limites
CREATE TABLE IF NOT EXISTS `tb_limites` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Valor` float NOT NULL,
  `Usuario_Id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FKg3nh9xiigfjv7m7wr775tswoc` (`Usuario_Id`),
  CONSTRAINT `FKg3nh9xiigfjv7m7wr775tswoc` FOREIGN KEY (`Usuario_Id`) REFERENCES `tb_usuarios` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Copiando dados para a tabela new_dwarf_control.tb_limites: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `tb_limites` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_limites` ENABLE KEYS */;

-- Copiando estrutura para tabela new_dwarf_control.tb_quandopagar
CREATE TABLE IF NOT EXISTS `tb_quandopagar` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Data` date NOT NULL,
  `Descricao` varchar(500) NOT NULL,
  `Status` bit(1) NOT NULL,
  `Valor` float NOT NULL,
  `Usuario_Id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FKg6fq53yriqqbrsgvdqh21dkee` (`Usuario_Id`),
  CONSTRAINT `FKg6fq53yriqqbrsgvdqh21dkee` FOREIGN KEY (`Usuario_Id`) REFERENCES `tb_usuarios` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Copiando dados para a tabela new_dwarf_control.tb_quandopagar: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `tb_quandopagar` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_quandopagar` ENABLE KEYS */;

-- Copiando estrutura para tabela new_dwarf_control.tb_usuarios
CREATE TABLE IF NOT EXISTS `tb_usuarios` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `DataHora_Criacao` datetime NOT NULL,
  `Email` varchar(255) NOT NULL,
  `Nivel` varchar(255) NOT NULL,
  `Nome` varchar(255) NOT NULL,
  `Senha` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `UK_ck0m3hr0qub6y318sfj4rebub` (`Email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Copiando dados para a tabela new_dwarf_control.tb_usuarios: ~2 rows (aproximadamente)
/*!40000 ALTER TABLE `tb_usuarios` DISABLE KEYS */;
INSERT INTO `tb_usuarios` (`Id`, `DataHora_Criacao`, `Email`, `Nivel`, `Nome`, `Senha`) VALUES
	(1, '2018-06-26 15:22:20', 'thiago.teodoro.rodrigues@gmail.com', 'ADMINISTRADOR', 'Thiago Teodoro Rodrigues', 'dfb1cbb2f4e9f1d0f7ae93387cc235ac');
/*!40000 ALTER TABLE `tb_usuarios` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
