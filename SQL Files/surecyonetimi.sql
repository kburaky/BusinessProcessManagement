-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Anamakine: localhost
-- Üretim Zamanı: 31 Jan 2018, 19:50:06
-- Sunucu sürümü: 5.6.14
-- PHP Sürümü: 5.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `surecyonetimi`
--
CREATE DATABASE IF NOT EXISTS `surecyonetimi` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `surecyonetimi`;

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `isSurecKulPro`(IN `kid` INT)
BEGIN
	
	SET @kid = kid;
	
	select * from isSureci as isu left join kullanici as kl on kl.kid = isu.kid left join kullanici as pkl on pkl.kid = isu.pid where isu.pid = @kid;

	
	
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `isSurecPro`()
BEGIN

	select * from isSureci as isu left join kullanici as kl on kl.kid = isu.kid left join kullanici as pkl on pkl.kid = isu.pid where isu.sDurum = 0 or isu.sDurum = 2;


END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `istatistikKulPro`(IN `kid` INT)
BEGIN
	
	SET @kid = kid;
	
	SELECT COUNT(sid) as bitirilenisToplam, (SELECT COUNT(sid) FROM issureci WHERE sDurum = 0 and pid = @kid) as aktifisToplam, (SELECT COUNT(mid) FROM surecmesajlari WHERE pid = @kid ) as mesajToplam ,  (SELECT COUNT(sid) FROM issureci WHERE bitisTarihi < now() and pid = @kid) as gecenisToplam FROM issureci WHERE sDurum = 1 AND pid = @kid;
	

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `istatistikPro`()
BEGIN
	
	
	SELECT COUNT(sid) as bitirilenisToplam, (SELECT COUNT(sid) FROM issureci WHERE sDurum = 0) as aktifisToplam, (SELECT COUNT(mid) FROM surecmesajlari) as mesajToplam ,  (SELECT COUNT(sid) FROM issureci WHERE bitisTarihi < now()) as gecenisToplam FROM issureci WHERE sDurum = 1;
	
	
	
	
	
	
	
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `isgruplari`
--

CREATE TABLE IF NOT EXISTS `isgruplari` (
  `iid` int(11) NOT NULL AUTO_INCREMENT,
  `iadi` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `gTarih` datetime NOT NULL,
  PRIMARY KEY (`iid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci AUTO_INCREMENT=8 ;

--
-- Tablo döküm verisi `isgruplari`
--

INSERT INTO `isgruplari` (`iid`, `iadi`, `gTarih`) VALUES
(2, 'Software', '2018-01-31 14:19:51'),
(4, 'Chat', '2018-01-31 14:26:09'),
(5, 'hyr', '2018-01-31 02:42:48'),
(6, 'Design', '2018-01-31 12:06:18'),
(7, 'Burak', '2018-01-31 14:36:34');

-- --------------------------------------------------------

--
-- Table structure for table `isgruplariatama`
--

CREATE TABLE IF NOT EXISTS `isgruplariatama` (
  `gid` int(11) NOT NULL AUTO_INCREMENT,
  `iid` int(11) NOT NULL,
  `pid` int(11) NOT NULL,
  `iTarih` datetime NOT NULL,
  PRIMARY KEY (`gid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci AUTO_INCREMENT=9 ;

--
-- Table dump data `isgruplariatama`
--

INSERT INTO `isgruplariatama` (`gid`, `iid`, `pid`, `iTarih`) VALUES
(1, 2, 3, '2018-01-31 12:00:30'),
(4, 4, 3, '2018-01-31 12:04:12'),
(6, 2, 2, '2018-01-31 12:05:45'),
(7, 4, 2, '2018-01-31 12:05:45'),
(8, 6, 2, '2018-01-31 12:06:33');

-- --------------------------------------------------------

--
-- Table structure for table `issureci`
--

CREATE TABLE IF NOT EXISTS `issureci` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `kid` int(11) NOT NULL COMMENT 'Müdürün id''si',
  `pid` int(11) NOT NULL COMMENT 'Personel id''s',
  `sBaslik` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `sAciklama` text COLLATE utf8_turkish_ci NOT NULL,
  `baslamaTarihi` datetime NOT NULL,
  `bitisTarihi` datetime NOT NULL,
  `sDurum` int(1) NOT NULL COMMENT '0 ise iş başladı, 1 ise bitti, 2 ise askıya alındı',
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci AUTO_INCREMENT=10 ;

--
-- Table dump data `issureci`
--

INSERT INTO `issureci` (`sid`, `kid`, `pid`, `sBaslik`, `sAciklama`, `baslamaTarihi`, `bitisTarihi`, `sDurum`) VALUES
(1, 1, 2, 'Garanti Tasarım-', '<p>Tasarım ayrıntılı a&ccedil;ıklama</p>\r\n\r\n<hr />\r\n<p><a href="http://google.com.tr">Google A&ccedil;</a></p>\r\n', '2018-01-31 07:30:00', '2018-02-01 07:30:00', 1),
(3, 1, 3, 'Afiş etiketi', '<p>Afiş i&ccedil;in gerekli işlemleri yapınız</p>\r\n', '2018-01-31 12:00:00', '2018-02-01 11:00:00', 0),
(4, 1, 3, 'Etiklet çalışması', '<p>Etikete hemen ihtiyacımız var</p>\r\n', '2018-01-31 12:00:00', '2018-02-01 11:00:00', 0),
(5, 1, 3, 'Etiketci', '<p>etiket <span class="marker">işleri</span>,&nbsp;</p>\r\n\r\n<hr />\r\n<p>ve diğer&nbsp;</p>\r\n', '2018-01-31 12:00:00', '2018-02-01 11:59:59', 2),
(6, 1, 2, 'Garanti Tasarım', '<p>Garanti bankası i&ccedil;in &ouml;rnek yapılacak tasarım ayrıntısı</p>\r\n', '2018-01-31 12:00:00', '2018-02-01 11:00:00', 1),
(8, 1, 4, 'Suat Pardon ama böyle insanlarda var', '<p>bu sana umarım &ouml;rnek olur.</p>\r\n\r\n<table border="1" cellpadding="1" cellspacing="1" style="width:500px">\r\n	<tbody>\r\n		<tr>\r\n			<td>Bak g&ouml;r</td>\r\n			<td><img alt="" src="http://i.hurimg.com/i/hurriyet/75/620x350/5677c9f367b0a95998cebdc4.jpg" style="height:350px; width:620px" /></td>\r\n		</tr>\r\n		<tr>\r\n			<td>Yukarı bak</td>\r\n			<td>&nbsp;</td>\r\n		</tr>\r\n	</tbody>\r\n</table>\r\n\r\n<p>&nbsp;</p>\r\n', '2018-01-31 12:00:00', '2018-02-01 11:59:59', 0),
(9, 1, 5, 'Projeyi Gelistir', '<p>Yazılım projemizi gelistir BURAK&nbsp;</p>\r\n', '2018-01-31 12:00:00', '2018-02-03 12:00:00', 1);

-- --------------------------------------------------------

--
-- Table structure for table `kullanici`
--

CREATE TABLE IF NOT EXISTS `kullanici` (
  `kid` int(11) NOT NULL AUTO_INCREMENT,
  `kUnvan` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `kSeviye` int(1) NOT NULL,
  `kAdi` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `kSoyadi` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `kMail` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `kSifre` varchar(32) COLLATE utf8_turkish_ci NOT NULL,
  `kTelefon` varchar(15) COLLATE utf8_turkish_ci NOT NULL,
  `kAdres` text COLLATE utf8_turkish_ci NOT NULL,
  `kTarih` datetime NOT NULL,
  PRIMARY KEY (`kid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci AUTO_INCREMENT=6 ;

--
-- Table dump data `kullanici`
--

INSERT INTO `kullanici` (`kid`, `kUnvan`, `kSeviye`, `kAdi`, `kSoyadi`, `kMail`, `kSifre`, `kTelefon`, `kAdres`, `kTarih`) VALUES
(1, 'Utopian Company', 0, 'Burak', 'Yilmaz', 'kburaky@steemit.com', '827ccb0eea8a706c4c34a16891f84e7b', '05435556688', 'Adana', '2017-08-13 06:19:18'),
(2, 'Designer', 2, 'Hasan', 'Bilsin', 'hasan@has.com', '827ccb0eea8a706c4c34a16891f84e7b', '090086073', 'Sivas', '2017-08-27 06:24:29'),
(3, 'Tag', 2, 'Ayşe', 'Bilir', 'ayse@mail.com', '827ccb0eea8a706c4c34a16891f84e7b', '876543', 'kljhgfd', '2017-09-16 11:14:19'),
(4, 'Suat Şirketi', 2, 'Suat', 'Cezik', 'suatcezik@gmail.com', '827ccb0eea8a706c4c34a16891f84e7b', '87654', 'kj', '2017-10-01 13:28:59'),
(5, 'Software Developer', 2, 'Burak', 'Yilmaz', 'kburaky@yandex.com', '827ccb0eea8a706c4c34a16891f84e7b', '05361234589', 'deneme', '2018-01-31 15:25:35');

-- --------------------------------------------------------

--
-- Table structure for table `surecmesajlari`
--

CREATE TABLE IF NOT EXISTS `surecmesajlari` (
  `mid` int(11) NOT NULL AUTO_INCREMENT,
  `sid` int(11) NOT NULL COMMENT 'Süreç id''si burada tutulacak',
  `gonderenID` int(11) NOT NULL,
  `aliciID` int(11) NOT NULL,
  `mesajText` text COLLATE utf8_turkish_ci NOT NULL,
  `mesajDosya` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `okunduDurum` int(1) NOT NULL COMMENT '0 ise okunmadı, 1 ise okundu',
  `mTarih` datetime NOT NULL,
  PRIMARY KEY (`mid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci AUTO_INCREMENT=8 ;

--
-- Table dump data `surecmesajlari`
--

INSERT INTO `surecmesajlari` (`mid`, `sid`, `gonderenID`, `aliciID`, `mesajText`, `mesajDosya`, `okunduDurum`, `mTarih`) VALUES
(1, 6, 1, 2, 'merhaba suat sen nasıl olurda bu hatayı yaparsın :)))', '', 0, '2018-01-31 13:01:38'),
(2, 6, 2, 1, 'slm ben hasan, suat akıllı dur', '', 0, '2018-01-31 13:03:02'),
(3, 6, 4, 2, 'mesaj alınsın', '', 0, '2018-01-31 13:29:31'),
(4, 6, 2, 1, 'cevap verildi', '', 0, '2018-01-31 13:30:27'),
(5, 6, 1, 2, 'sen ve suat ne kadar ', '', 0, '2018-01-31 14:04:55'),
(6, 6, 1, 2, 'sevgili suat, bir gece ansızın gelebilir.', '', 0, '2018-01-31 14:08:25'),
(7, 6, 1, 2, 'oalal', '', 0, '2018-01-31 10:15:12');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
