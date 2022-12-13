package com.example.demo.Export.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Export.AreaExportExcel;
import com.example.demo.Export.BrandExportExcel;
import com.example.demo.Export.CategoryExportExcel;
import com.example.demo.Export.CityExportExcel;
import com.example.demo.Export.CountryExportExcel;
import com.example.demo.Export.DistributorExportExcel;
import com.example.demo.Export.DistrictExportExcel;
import com.example.demo.Export.FamilyExportExcel;
import com.example.demo.Export.HqExportExcel;
import com.example.demo.Export.ProductExportExcel;
import com.example.demo.Export.RegionExportExcel;
import com.example.demo.Export.StateExportExcel;
import com.example.demo.entity.Area;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Category;
import com.example.demo.entity.City;
import com.example.demo.entity.Country;
import com.example.demo.entity.Distributor;
import com.example.demo.entity.District;
import com.example.demo.entity.Family;
import com.example.demo.entity.HqMaster;
import com.example.demo.entity.Product;
import com.example.demo.entity.Region;
import com.example.demo.entity.State;
import com.example.demo.repository.AreaRepository;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.DistRepository;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.repository.FamilyRepository;
import com.example.demo.repository.HqRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.repository.StateRepository;

@Service
public class ExportService {

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	StateRepository stateRepository;

	@Autowired
	RegionRepository regionRepository;

	@Autowired
	DistrictRepository districtRepository;

	@Autowired
	CityRepository cityRepository;

	@Autowired
	AreaRepository areaRepository;

	@Autowired
	HqRepository hqRepository;

	@Autowired
	BrandRepository brandRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	FamilyRepository familyRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	DistRepository distRepository;

	public ByteArrayInputStream countryArrayInputStream() {
		List<Country> countries = countryRepository.findAll();

		// CountryExportExcel countryExportExcel = new CountryExportExcel();
		ByteArrayInputStream in = CountryExportExcel.countriesToExcel(countries);
		return in;
	}

	public ByteArrayInputStream stateArrayInputStream() {
		List<State> states = stateRepository.findAll();

		// CountryExportExcel countryExportExcel = new CountryExportExcel();
		ByteArrayInputStream in = StateExportExcel.statesToExcel(states);
		return in;
	}

	public ByteArrayInputStream regionArrayInputStream() {
		List<Region> regions = regionRepository.findAll();

		// CountryExportExcel countryExportExcel = new CountryExportExcel();
		ByteArrayInputStream in = RegionExportExcel.regionToExcel(regions);
		return in;
	}

	public ByteArrayInputStream districtArrayInputStream() {
		List<District> districts = districtRepository.findAll();

		// CountryExportExcel countryExportExcel = new CountryExportExcel();
		ByteArrayInputStream in = DistrictExportExcel.districtToExcel(districts);
		return in;
	}

	public ByteArrayInputStream cityArrayInputStream() {
		List<City> cities = cityRepository.findAll();

		// CountryExportExcel countryExportExcel = new CountryExportExcel();
		ByteArrayInputStream in = CityExportExcel.cityToExcel(cities);
		return in;
	}

	public ByteArrayInputStream areaArrayInputStream() {
		List<Area> areas = areaRepository.findAll();

		// CountryExportExcel countryExportExcel = new CountryExportExcel();
		ByteArrayInputStream in = AreaExportExcel.areaToExcel(areas);
		return in;
	}

	public ByteArrayInputStream hqArrayInputStream() {
		List<HqMaster> hqMasters = hqRepository.findAll();

		// CountryExportExcel countryExportExcel = new CountryExportExcel();
		ByteArrayInputStream in = HqExportExcel.hqToExcel(hqMasters);
		return in;
	}

	public ByteArrayInputStream brandInputStream() {
		List<Brand> brands = brandRepository.findAll();

		// CountryExportExcel countryExportExcel = new CountryExportExcel();
		ByteArrayInputStream in = BrandExportExcel.brandToExcel(brands);
		return in;
	}

	public ByteArrayInputStream categoryInputStream() {
		List<Category> categories = categoryRepository.findAll();

		// CountryExportExcel countryExportExcel = new CountryExportExcel();
		ByteArrayInputStream in = CategoryExportExcel.categoryToExcel(categories);
		return in;
	}

	public ByteArrayInputStream familyArrayInputStream() {
		List<Family> families = familyRepository.findAll();

		// CountryExportExcel countryExportExcel = new CountryExportExcel();
		ByteArrayInputStream in = FamilyExportExcel.familyToExcel(families);
		return in;
	}

	public ByteArrayInputStream productArrayInputStream() {
		List<Product> products = productRepository.findAll();

		// CountryExportExcel countryExportExcel = new CountryExportExcel();
		ByteArrayInputStream in = ProductExportExcel.productToExcel(products);
		return in;
	}

	public ByteArrayInputStream distributorArrayInputStream() {
		List<Distributor> distributors = distRepository.findAll();

		// CountryExportExcel countryExportExcel = new CountryExportExcel();
		ByteArrayInputStream in = DistributorExportExcel.distributorToExcel(distributors);
		return in;
	}
}
