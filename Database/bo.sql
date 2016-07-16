-- phpMyAdmin SQL Dump
-- version 4.4.12
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 14 Jan 2016 pada 06.06
-- Versi Server: 5.6.25
-- PHP Version: 5.6.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bo`
--
CREATE DATABASE IF NOT EXISTS `bo` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `bo`;

-- --------------------------------------------------------

--
-- Struktur dari tabel `barang`
--

CREATE TABLE IF NOT EXISTS `barang` (
  `IDBRG` char(5) NOT NULL,
  `IDPEMBELIAN` char(5) DEFAULT NULL,
  `IDPENJUALAN` char(5) DEFAULT NULL,
  `NAMABRG` varchar(50) DEFAULT NULL,
  `HRGBELI` int(11) DEFAULT NULL,
  `HARGAJUAL` int(11) DEFAULT NULL,
  `STOK` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `barang`
--

INSERT INTO `barang` (`IDBRG`, `IDPEMBELIAN`, `IDPENJUALAN`, `NAMABRG`, `HRGBELI`, `HARGAJUAL`, `STOK`) VALUES
('B0001', NULL, NULL, 'Koala Kumal', 50000, 75000, 15),
('B0002', NULL, NULL, 'Kreatif Sampai Mati', 65000, 80000, 12),
('B0003', NULL, NULL, 'Sepatu Dahlan', 80000, 120000, 0),
('B0004', NULL, NULL, 'Rahvayana', 55000, 65000, 13),
('B0005', NULL, NULL, 'Perahu Kertas', 90000, 120000, 15),
('B0006', NULL, NULL, 'Negeri 5 Menara', 90000, 160000, 10),
('B0007', NULL, NULL, 'Mimpi Sejuta Dollar', 76000, 100000, 16),
('B0008', NULL, NULL, 'Cahaya Langit Eropa', 90000, 132000, 11);

-- --------------------------------------------------------

--
-- Struktur dari tabel `detailpembelian`
--

CREATE TABLE IF NOT EXISTS `detailpembelian` (
  `No` int(11) NOT NULL,
  `IDPEMBELIAN` char(5) NOT NULL,
  `NAMASUPPLIER` varchar(50) NOT NULL,
  `NAMABRG` varchar(50) NOT NULL,
  `JUMLAH` int(11) NOT NULL,
  `HARGA` int(11) NOT NULL,
  `SUBHARGA` int(11) NOT NULL,
  `BAYAR` int(11) NOT NULL,
  `KEMBALI` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `detailpembelian`
--

INSERT INTO `detailpembelian` (`No`, `IDPEMBELIAN`, `NAMASUPPLIER`, `NAMABRG`, `JUMLAH`, `HARGA`, `SUBHARGA`, `BAYAR`, `KEMBALI`) VALUES
(2, 'PM01', 'Grasindo', 'Kreatif Sampai Mati', 1, 65000, 65000, 145000, 65000),
(3, 'PM01', 'Grasindo', 'Sepatu Dahlan', 1, 80000, 80000, 145000, 65000),
(4, 'PM02', 'Grasindo', 'Koala Kumal', 2, 50000, 100000, 100000, 0),
(5, 'PM03', 'Pustaka Zahra', 'Kreatif Sampai Mati', 2, 65000, 130000, 150000, 20000),
(6, 'PM04', 'Pustaka Zahra', 'Rahvayana', 1, 55000, 55000, 185000, 55000),
(7, 'PM04', 'Grasindo', 'Kreatif Sampai Mati', 2, 65000, 130000, 185000, 55000),
(8, 'PM05', 'Grasindo', 'Sepatu Dahlan', 2, 80000, 160000, 160000, 0),
(9, 'PM06', 'Grasindo', 'Kreatif Sampai Mati', 2, 65000, 130000, 400000, 0),
(10, 'PM06', 'PT. Gramedia ', 'Sepatu Dahlan', 2, 80000, 160000, 400000, 0),
(11, 'PM06', 'PT. Gramedia ', 'Rahvayana', 2, 55000, 110000, 400000, 0),
(12, 'PM07', 'Pustaka Zahra', 'Koala Kumal', 10, 50000, 500000, 500000, 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `detailpenjualan`
--

CREATE TABLE IF NOT EXISTS `detailpenjualan` (
  `No` int(11) NOT NULL,
  `IDPENJUALAN` char(5) NOT NULL,
  `NAMAKARYAWAN` varchar(50) NOT NULL,
  `NAMABRG` varchar(50) NOT NULL,
  `sJumlah` int(11) NOT NULL,
  `HARGA` int(11) NOT NULL,
  `SUBHARGA` int(11) NOT NULL,
  `BAYAR` int(11) DEFAULT NULL,
  `KEMBALI` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `detailpenjualan`
--

INSERT INTO `detailpenjualan` (`No`, `IDPENJUALAN`, `NAMAKARYAWAN`, `NAMABRG`, `sJumlah`, `HARGA`, `SUBHARGA`, `BAYAR`, `KEMBALI`) VALUES
(5, 'PJ01', 'Antoni', 'Kreatif Sampai Mati', 1, 80000, 80000, 255000, 155000),
(6, 'PJ01', 'Antoni', 'Koala Kumal', 1, 75000, 75000, 255000, 155000),
(7, 'PJ01', 'Antoni', 'Mimpi Sejuta Dollar', 1, 100000, 100000, 255000, 155000),
(8, 'PJ02', 'Bimbi', 'Kreatif Sampai Mati', 4, 80000, 320000, 350000, 30000),
(9, 'PJ03', 'Bimbi', 'Kreatif Sampai Mati', 2, 80000, 160000, 160000, 0),
(10, 'PJ04', 'Bimbi', 'Kreatif Sampai Mati', 1, 80000, 80000, 80000, 0),
(11, 'PJ05', 'Antoni', 'Kreatif Sampai Mati', 9, 80000, 720000, 720000, 0),
(12, 'PJ06', 'Antoni', 'Koala Kumal', 2, 75000, 150000, 150000, 0),
(13, 'PJ07', 'Bimbi', 'Kreatif Sampai Mati', 2, 80000, 160000, 385000, 160000),
(14, 'PJ07', 'Bimbi', 'Koala Kumal', 3, 75000, 225000, 385000, 160000),
(15, 'PJ08', 'Antoni', 'Koala Kumal', 2, 75000, 150000, 510000, 0),
(16, 'PJ08', 'Antoni', 'Kreatif Sampai Mati', 3, 80000, 240000, 510000, 0),
(17, 'PJ08', 'Antoni', 'Perahu Kertas', 1, 120000, 120000, 510000, 0),
(18, 'PJ09', 'Antoni', 'Koala Kumal', 1, 75000, 75000, 2360000, 5000),
(19, 'PJ09', 'Antoni', 'Sepatu Dahlan', 19, 120000, 2280000, 2360000, 5000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `karyawan`
--

CREATE TABLE IF NOT EXISTS `karyawan` (
  `IDKARYAWAN` char(6) NOT NULL,
  `NAMAKARYAWAN` varchar(50) DEFAULT NULL,
  `JNSKELAMIN` varchar(20) DEFAULT NULL,
  `TELPKAR` char(12) DEFAULT NULL,
  `ALAMAT` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `karyawan`
--

INSERT INTO `karyawan` (`IDKARYAWAN`, `NAMAKARYAWAN`, `JNSKELAMIN`, `TELPKAR`, `ALAMAT`) VALUES
('R0001', 'Antoni', 'Laki - Laki', '082918281012', 'Mataram'),
('R0002', 'Bimbi', 'Laki - Laki', '0321827374', 'Yogyakarta'),
('R0003', 'Cintia', 'Perempuan', '087123123123', 'Surabaya'),
('R0004', 'Dinada', 'Perempuan', '098872738182', 'Bandung');

-- --------------------------------------------------------

--
-- Struktur dari tabel `konsumen`
--

CREATE TABLE IF NOT EXISTS `konsumen` (
  `IDKONSUMEN` char(5) NOT NULL,
  `NAMAKONSUMEN` char(50) DEFAULT NULL,
  `JENISKEL` char(20) DEFAULT NULL,
  `TELPKON` char(12) DEFAULT NULL,
  `ALAMATKONS` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `konsumen`
--

INSERT INTO `konsumen` (`IDKONSUMEN`, `NAMAKONSUMEN`, `JENISKEL`, `TELPKON`, `ALAMATKONS`) VALUES
('K0001', 'Steve Jobs', 'Laki - Laki', '14023', 'California'),
('K0002', 'Bill Gates', 'Laki - Laki', '98623', 'Los Angeles'),
('K0003', 'Marry Anderson', 'Perempuan', '12365', 'London'),
('K0004', 'Ariel Tatum', 'Perempuan', '12312', 'Jakarta'),
('K0005', 'Ridwan Kamil', 'Laki - Laki', '33451', 'Bandung');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pembelian`
--

CREATE TABLE IF NOT EXISTS `pembelian` (
  `IDPEMBELIAN` char(5) NOT NULL,
  `IDKARYAWAN` char(6) DEFAULT NULL,
  `IDSUPPLIER` char(5) DEFAULT NULL,
  `TGLPEMBELIAN` datetime DEFAULT NULL,
  `TOTALPEMBELIAN` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `pembelian`
--

INSERT INTO `pembelian` (`IDPEMBELIAN`, `IDKARYAWAN`, `IDSUPPLIER`, `TGLPEMBELIAN`, `TOTALPEMBELIAN`) VALUES
('PM01', NULL, NULL, '2016-01-05 19:53:44', 145000),
('PM02', NULL, NULL, '2016-01-05 00:00:00', 100000),
('PM03', NULL, NULL, '2016-01-05 00:00:00', 130000),
('PM04', NULL, NULL, '2016-01-07 00:00:00', 185000),
('PM05', NULL, NULL, '2016-01-07 00:00:00', 160000),
('PM06', NULL, NULL, '2016-01-14 00:00:00', 400000),
('PM07', NULL, NULL, '2016-01-14 00:00:00', 500000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `penjualan`
--

CREATE TABLE IF NOT EXISTS `penjualan` (
  `IDPENJUALAN` char(5) NOT NULL,
  `IDKARYAWAN` char(6) DEFAULT NULL,
  `IDKONSUMEN` char(5) DEFAULT NULL,
  `TGLPENJUALAN` datetime DEFAULT NULL,
  `TOTALPENJUALAN` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `penjualan`
--

INSERT INTO `penjualan` (`IDPENJUALAN`, `IDKARYAWAN`, `IDKONSUMEN`, `TGLPENJUALAN`, `TOTALPENJUALAN`) VALUES
('PJ01', NULL, NULL, '2016-01-05 00:00:00', 255000),
('PJ02', NULL, NULL, '2016-01-05 00:00:00', 320000),
('PJ03', NULL, NULL, '2016-01-05 00:00:00', 160000),
('PJ04', NULL, NULL, '2016-01-06 00:00:00', 80000),
('PJ05', NULL, NULL, '2016-01-07 00:00:00', 720000),
('PJ06', NULL, NULL, '2016-01-09 00:00:00', 150000),
('PJ07', NULL, NULL, '2016-01-09 00:00:00', 385000),
('PJ08', NULL, NULL, '2016-01-09 00:00:00', 510000),
('PJ09', NULL, NULL, '2016-01-14 00:00:00', 2355000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `supplier`
--

CREATE TABLE IF NOT EXISTS `supplier` (
  `IDSUPPLIER` char(5) NOT NULL,
  `NAMASUPPLIER` varchar(50) DEFAULT NULL,
  `KETERANGAN` varchar(100) DEFAULT NULL,
  `AMALAT` varchar(100) DEFAULT NULL,
  `TELSUP` char(12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `supplier`
--

INSERT INTO `supplier` (`IDSUPPLIER`, `NAMASUPPLIER`, `KETERANGAN`, `AMALAT`, `TELSUP`) VALUES
('S0001', 'Pustaka Zahra', 'Novel', 'Jl. Batu Ampar III No. 14 Condet Jakarta Timur', '021-8092269'),
('S0002', 'Grasindo', 'Buku Sejarah', 'Jl. Palmerah Selatan 22', '021-5483008'),
('S0003', 'Aditya Media', 'Buku Cerita Pendek', 'Jl. Bimasakti GK I/19, Yogyakarta 55221', '031-520613'),
('S0004', 'PT. Gramedia ', 'Buku Pendidikan', 'Jl. Palmerah Selatan 22 Lantai IV ', '0274-865557'),
('S0005', 'TransMedia', 'Novel', 'JL. H. Montong, No. 57, Ciganjur, Jagakarsa, 12630', '022-1788830');

-- --------------------------------------------------------

--
-- Struktur dari tabel `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `no` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `user`
--

INSERT INTO `user` (`no`, `username`, `password`) VALUES
(2, 'admin', 'admin'),
(3, 'akukamu', 'akukamu');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `barang`
--
ALTER TABLE `barang`
  ADD PRIMARY KEY (`IDBRG`),
  ADD KEY `FK_MEMILIKI` (`IDPENJUALAN`),
  ADD KEY `FK_MILIK` (`IDPEMBELIAN`);

--
-- Indexes for table `detailpembelian`
--
ALTER TABLE `detailpembelian`
  ADD PRIMARY KEY (`No`);

--
-- Indexes for table `detailpenjualan`
--
ALTER TABLE `detailpenjualan`
  ADD PRIMARY KEY (`No`);

--
-- Indexes for table `karyawan`
--
ALTER TABLE `karyawan`
  ADD PRIMARY KEY (`IDKARYAWAN`);

--
-- Indexes for table `konsumen`
--
ALTER TABLE `konsumen`
  ADD PRIMARY KEY (`IDKONSUMEN`);

--
-- Indexes for table `pembelian`
--
ALTER TABLE `pembelian`
  ADD PRIMARY KEY (`IDPEMBELIAN`),
  ADD KEY `FK_MEMPUNYAI` (`IDKARYAWAN`),
  ADD KEY `FK_MENDAPATKAN` (`IDSUPPLIER`);

--
-- Indexes for table `penjualan`
--
ALTER TABLE `penjualan`
  ADD PRIMARY KEY (`IDPENJUALAN`),
  ADD KEY `FK_MELAKUKAN` (`IDKONSUMEN`),
  ADD KEY `FK_MENGHASILKAN` (`IDKARYAWAN`);

--
-- Indexes for table `supplier`
--
ALTER TABLE `supplier`
  ADD PRIMARY KEY (`IDSUPPLIER`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`no`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `detailpembelian`
--
ALTER TABLE `detailpembelian`
  MODIFY `No` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=13;
--
-- AUTO_INCREMENT for table `detailpenjualan`
--
ALTER TABLE `detailpenjualan`
  MODIFY `No` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=20;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `no` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `barang`
--
ALTER TABLE `barang`
  ADD CONSTRAINT `FK_MEMILIKI` FOREIGN KEY (`IDPENJUALAN`) REFERENCES `penjualan` (`IDPENJUALAN`),
  ADD CONSTRAINT `FK_MILIK` FOREIGN KEY (`IDPEMBELIAN`) REFERENCES `pembelian` (`IDPEMBELIAN`);

--
-- Ketidakleluasaan untuk tabel `pembelian`
--
ALTER TABLE `pembelian`
  ADD CONSTRAINT `FK_MEMPUNYAI` FOREIGN KEY (`IDKARYAWAN`) REFERENCES `karyawan` (`IDKARYAWAN`),
  ADD CONSTRAINT `FK_MENDAPATKAN` FOREIGN KEY (`IDSUPPLIER`) REFERENCES `supplier` (`IDSUPPLIER`);

--
-- Ketidakleluasaan untuk tabel `penjualan`
--
ALTER TABLE `penjualan`
  ADD CONSTRAINT `FK_MELAKUKAN` FOREIGN KEY (`IDKONSUMEN`) REFERENCES `konsumen` (`IDKONSUMEN`),
  ADD CONSTRAINT `FK_MENGHASILKAN` FOREIGN KEY (`IDKARYAWAN`) REFERENCES `karyawan` (`IDKARYAWAN`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
