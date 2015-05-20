-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: May 18, 2015 at 10:55 AM
-- Server version: 5.6.20
-- PHP Version: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `ece_pharmacy_tree`
--

-- --------------------------------------------------------

--
-- Table structure for table `baskets`
--

CREATE TABLE IF NOT EXISTS `baskets` (
`id` int(11) NOT NULL,
  `patient_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `billings`
--

CREATE TABLE IF NOT EXISTS `billings` (
`id` int(11) NOT NULL,
  `order_id` int(11) DEFAULT NULL,
  `gross_total` double DEFAULT NULL,
  `total` double DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `clinics`
--

CREATE TABLE IF NOT EXISTS `clinics` (
`id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `address_house_no` varchar(10) DEFAULT NULL,
  `address_street` varchar(50) DEFAULT NULL,
  `address_barangay` varchar(50) DEFAULT NULL,
  `address_city_municipality` varchar(50) DEFAULT NULL,
  `address_province` varchar(50) DEFAULT NULL,
  `address_region` varchar(50) DEFAULT NULL,
  `address_zip` varchar(10) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `doctors`
--

CREATE TABLE IF NOT EXISTS `doctors` (
`id` int(11) NOT NULL,
  `lname` varchar(255) DEFAULT NULL,
  `mname` varchar(255) DEFAULT NULL,
  `fname` varchar(255) DEFAULT NULL,
  `prc_no` int(11) DEFAULT NULL,
  `address_house_no` varchar(20) DEFAULT NULL,
  `address_street` varchar(50) DEFAULT NULL,
  `address_barangay` varchar(50) DEFAULT NULL,
  `address_city_municipality` varchar(50) DEFAULT NULL,
  `address_province` varchar(50) DEFAULT NULL,
  `address_region` varchar(50) DEFAULT NULL,
  `address_zip` varchar(10) DEFAULT NULL,
  `address_country` varchar(100) DEFAULT NULL,
  `specialty` varchar(255) NOT NULL,
  `sub_specialty` varchar(255) DEFAULT NULL,
  `cell_no` varchar(14) DEFAULT NULL,
  `tel_no` varchar(8) DEFAULT NULL,
  `photo` longtext,
  `clinic_sched` longtext,
  `affiliation` longtext,
  `clinic_id` int(11) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `secretary_id` int(11) DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `doctors`
--

INSERT INTO `doctors` (`id`, `lname`, `mname`, `fname`, `prc_no`, `address_house_no`, `address_street`, `address_barangay`, `address_city_municipality`, `address_province`, `address_region`, `address_zip`, `address_country`, `specialty`, `sub_specialty`, `cell_no`, `tel_no`, `photo`, `clinic_sched`, `affiliation`, `clinic_id`, `email`, `created_at`, `updated_at`, `deleted_at`, `secretary_id`) VALUES
(1, 'Bengil', 'M', 'Dexter', 132432, '56', 'Leviticus', 'Cabantian', 'Davao City', 'Davao del Sur', 'XI', '8000', 'Phillippines', 'Cook', 'Rice Cooker', '09325438543', '93563453', 'sadsadsad', 'dasfsa', 'dasd', 1, 'dsadasds', '2015-05-14 02:01:13', '2015-05-14 03:14:00', NULL, 1),
(2, 'Bengil', 'M', 'Dexxx', 13432556, '56', 'Leviticus', 'Cabantian', 'Davao City', 'Davao del Sur', 'XI', '8000', 'Phillippines', 'Computer Software Specialist and Doctor of Technology', 'Rice Cooker', '09325438543', '93563453', 'sadsadsad', 'dasfsa', 'dasd', 1, 'dsadasds', '2015-05-14 03:14:13', '2015-05-15 03:30:00', NULL, 1),
(3, 'Bengil', 'M', 'Dex', 1324321332, '56', 'Leviticus', 'Cabantian', 'Davao City', 'Davao del Sur', 'XI', '8000', 'Phillippines', 'Computer Software Specialist and Doctor of Technology', 'Rice Cooker', '09325438543', '93563453', 'sadsadsad', 'dasfsa', 'dasd', 1, 'dsadasds', '2015-05-14 03:14:13', '2015-05-15 03:30:00', NULL, 1);

-- --------------------------------------------------------

--
-- Table structure for table `doctor_referrals`
--

CREATE TABLE IF NOT EXISTS `doctor_referrals` (
`id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `referred_by` int(11) NOT NULL,
  `referred_to` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `employees`
--

CREATE TABLE IF NOT EXISTS `employees` (
`id` int(11) NOT NULL,
  `fname` varchar(50) DEFAULT NULL,
  `mname` varchar(50) DEFAULT NULL,
  `lname` varchar(50) DEFAULT NULL,
  `position` varchar(50) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE IF NOT EXISTS `orders` (
`id` int(11) NOT NULL,
  `patient_id` int(11) DEFAULT NULL,
  `delivery_sched` date DEFAULT NULL,
  `address_house_no` varchar(10) DEFAULT NULL,
  `address_street` varchar(50) DEFAULT NULL,
  `address_barangay` varchar(50) DEFAULT NULL,
  `address_city_municipality` varchar(50) DEFAULT NULL,
  `address_province` varchar(50) DEFAULT NULL,
  `address_region` varchar(50) DEFAULT NULL,
  `address_zip` varchar(10) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `order_details`
--

CREATE TABLE IF NOT EXISTS `order_details` (
`id` int(11) NOT NULL,
  `order_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `patients`
--

CREATE TABLE IF NOT EXISTS `patients` (
`id` int(11) NOT NULL,
  `fname` varchar(50) DEFAULT NULL,
  `mname` varchar(50) DEFAULT NULL,
  `lname` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `occupation` varchar(255) DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `sex` varchar(6) DEFAULT NULL,
  `civil_status` varchar(20) DEFAULT NULL,
  `height` varchar(10) DEFAULT NULL,
  `weight` varchar(10) DEFAULT NULL,
  `unit_floor_room_no` int(11) NOT NULL,
  `building` varchar(50) NOT NULL,
  `lot_no` int(11) NOT NULL,
  `block_no` int(11) NOT NULL,
  `phase_no` int(11) NOT NULL,
  `address_house_no` int(11) DEFAULT NULL,
  `address_street` varchar(50) DEFAULT NULL,
  `address_barangay` varchar(50) DEFAULT NULL,
  `address_city_municipality` varchar(50) DEFAULT NULL,
  `address_province` varchar(50) DEFAULT NULL,
  `address_region` varchar(50) DEFAULT NULL,
  `address_zip` varchar(50) DEFAULT NULL,
  `tel_no` varchar(8) NOT NULL,
  `cell_no` varchar(14) NOT NULL,
  `email` varchar(50) NOT NULL,
  `photo` longtext NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `patient_consultations`
--

CREATE TABLE IF NOT EXISTS `patient_consultations` (
`id` int(11) NOT NULL,
  `patient_id` int(11) DEFAULT NULL,
  `doctor_id` int(11) DEFAULT NULL,
  `schedule` varchar(2) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `patient_records`
--

CREATE TABLE IF NOT EXISTS `patient_records` (
`id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `complaints` longtext,
  `findings` longtext,
  `treatment_id` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE IF NOT EXISTS `payments` (
`id` int(11) NOT NULL,
  `billing_id` int(11) DEFAULT NULL,
  `payment_mode` varchar(255) DEFAULT NULL,
  `txn_id` varchar(255) DEFAULT NULL,
  `or_no` varchar(255) DEFAULT NULL,
  `employee_id` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE IF NOT EXISTS `products` (
`id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `dosage_format_and_strength` varchar(255) DEFAULT NULL,
  `generic_name` longtext,
  `description` longtext,
  `prescription_required` tinyint(1) NOT NULL DEFAULT '0',
  `price` double DEFAULT NULL,
  `unit` varchar(50) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `dosage_format_and_strength`, `generic_name`, `description`, `prescription_required`, `price`, `unit`, `created_at`, `updated_at`, `deleted_at`) VALUES
(1, 'Biogesic', '500 mg tablet', 'Paracetamol ', '- For the relief of minor aches and pains such as headache, backache, menstrua cramps, muscular aches, minor arthritis pain, toothache, and pain associated with common cold and flu.\r\n\r\n- For fever reduction.', 0, 3, 'tablet', '2015-05-18 02:54:44', NULL, NULL),
(2, 'Fluimucil', '100 mg granules for oral solution/ 200 mg granules for oral solution', 'Acetylcysteine ', 'Used in the treatment of respiratory affections characterized by thick and viscous hypersecretions such as bronchitis and its exacerbations, pulmonary emphysema, mucoviscidosis and bronchiectasis.', 1, 17.25, 'granule', '2015-05-18 03:03:31', NULL, NULL),
(3, 'Herpex', '200mg / 400mg / 800 mg tablet', 'Aciclovir ', 'Used in the treatment of some viral infections such as chickenpox, shingles, cold sores, and genital herpes.', 1, NULL, NULL, '2015-05-18 03:03:31', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `product_categories`
--

CREATE TABLE IF NOT EXISTS `product_categories` (
`id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `product_categories`
--

INSERT INTO `product_categories` (`id`, `name`, `created_at`, `updated_at`, `deleted_at`) VALUES
(1, 'over-the-counter-medicines', '2015-05-18 03:18:11', NULL, NULL),
(2, 'prescription drugs', '2015-05-18 03:18:11', NULL, NULL),
(3, 'vitamins & supplements', '2015-05-18 03:19:05', NULL, NULL),
(4, 'medical devices', '2015-05-18 03:19:05', NULL, NULL),
(7, 'sexual wellness', '2015-05-18 03:19:39', NULL, NULL),
(8, 'health & beauty', '2015-05-18 03:19:39', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `product_subcategories`
--

CREATE TABLE IF NOT EXISTS `product_subcategories` (
`id` int(11) NOT NULL,
  `name` text NOT NULL,
  `category_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

--
-- Dumping data for table `product_subcategories`
--

INSERT INTO `product_subcategories` (`id`, `name`, `category_id`, `created_at`, `updated_at`, `deleted_at`) VALUES
(1, 'allergies', 1, '2015-05-18 03:23:50', NULL, NULL),
(2, 'cough & colds', 1, '2015-05-18 03:23:50', NULL, NULL),
(3, 'deworming agents', 1, '2015-05-18 03:23:50', NULL, NULL),
(4, 'ear, nose, mouth & throat preparations', 1, '2015-05-18 03:23:50', NULL, NULL),
(5, 'eye preparations', 1, '2015-05-18 03:23:50', NULL, NULL),
(6, 'fever & pain relief', 1, '2015-05-18 03:23:50', NULL, NULL),
(7, 'hair & scalp', 1, '2015-05-18 03:23:50', NULL, NULL),
(8, 'allergies', 2, '2015-05-18 03:28:03', NULL, NULL),
(9, 'anti-infective agens', 2, '2015-05-18 03:28:03', NULL, NULL),
(10, 'apetite enhancers', 2, '2015-05-18 03:28:03', NULL, NULL),
(11, 'asthma & other airway diseases', 2, '2015-05-18 03:28:03', NULL, NULL),
(12, 'blood pressure & heart medications', 2, '2015-05-18 03:28:03', NULL, NULL),
(13, 'brain & nervous system', 2, '2015-05-18 03:28:03', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `promo_discounts`
--

CREATE TABLE IF NOT EXISTS `promo_discounts` (
`id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `name` int(11) DEFAULT NULL,
  `start_date` int(11) DEFAULT NULL,
  `end_date` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `quantity_required` int(11) DEFAULT NULL,
  `less` double DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `promo_free_products`
--

CREATE TABLE IF NOT EXISTS `promo_free_products` (
`id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `promo_id` int(11) DEFAULT NULL,
  `no_of_units_free` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `secretaries`
--

CREATE TABLE IF NOT EXISTS `secretaries` (
`id` int(11) NOT NULL,
  `fname` varchar(50) NOT NULL,
  `mname` varchar(50) NOT NULL,
  `lname` varchar(50) NOT NULL,
  `address_house_no` varchar(10) NOT NULL,
  `address_street` varchar(50) NOT NULL,
  `address_barangay` varchar(50) NOT NULL,
  `address_city_municipality` varchar(50) NOT NULL,
  `address_province` varchar(50) NOT NULL,
  `address_region` varchar(50) NOT NULL,
  `address_zip` varchar(10) NOT NULL,
  `cell_no` varchar(15) NOT NULL,
  `tel_no` varchar(8) NOT NULL,
  `email` varchar(50) NOT NULL,
  `photo` longtext,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

CREATE TABLE IF NOT EXISTS `settings` (
`id` int(11) NOT NULL,
  `referral_points` int(11) DEFAULT '1',
  `referral_level_limit` int(11) DEFAULT '2',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `treatments`
--

CREATE TABLE IF NOT EXISTS `treatments` (
`id` int(11) NOT NULL,
  `medicine_name` varchar(255) DEFAULT NULL,
  `generic_name` longtext,
  `quantity` varchar(11) DEFAULT NULL,
  `prescription` longtext,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `baskets`
--
ALTER TABLE `baskets`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `billings`
--
ALTER TABLE `billings`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `clinics`
--
ALTER TABLE `clinics`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `doctors`
--
ALTER TABLE `doctors`
 ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `prc_no` (`prc_no`);

--
-- Indexes for table `doctor_referrals`
--
ALTER TABLE `doctor_referrals`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `employees`
--
ALTER TABLE `employees`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `order_details`
--
ALTER TABLE `order_details`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `patients`
--
ALTER TABLE `patients`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `patient_consultations`
--
ALTER TABLE `patient_consultations`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `patient_records`
--
ALTER TABLE `patient_records`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `product_categories`
--
ALTER TABLE `product_categories`
 ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `product_subcategories`
--
ALTER TABLE `product_subcategories`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `promo_discounts`
--
ALTER TABLE `promo_discounts`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `promo_free_products`
--
ALTER TABLE `promo_free_products`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `secretaries`
--
ALTER TABLE `secretaries`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `settings`
--
ALTER TABLE `settings`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `treatments`
--
ALTER TABLE `treatments`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `baskets`
--
ALTER TABLE `baskets`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `billings`
--
ALTER TABLE `billings`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `clinics`
--
ALTER TABLE `clinics`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `doctors`
--
ALTER TABLE `doctors`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `doctor_referrals`
--
ALTER TABLE `doctor_referrals`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `employees`
--
ALTER TABLE `employees`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `order_details`
--
ALTER TABLE `order_details`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `patients`
--
ALTER TABLE `patients`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `patient_consultations`
--
ALTER TABLE `patient_consultations`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `patient_records`
--
ALTER TABLE `patient_records`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `product_categories`
--
ALTER TABLE `product_categories`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `product_subcategories`
--
ALTER TABLE `product_subcategories`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=14;
--
-- AUTO_INCREMENT for table `promo_discounts`
--
ALTER TABLE `promo_discounts`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `promo_free_products`
--
ALTER TABLE `promo_free_products`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `secretaries`
--
ALTER TABLE `secretaries`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `settings`
--
ALTER TABLE `settings`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `treatments`
--
ALTER TABLE `treatments`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
