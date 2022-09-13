package com.example.demo.App.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "cmatwo")
public class CmaTwo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long id;
	
	@Column(name = "cma_one_id", nullable = false, length = 20)
    private Long cma_one_id;
	
	@Column(name = "domestic_sales_commission_brokerage", nullable = false, length = 20)
    private String domestic_sales_commission_brokerage;
	
	@Column(name = "export_sales", nullable = false, length = 20)
    private String export_sales;
	
	@Column(name = "other_operating_income", nullable = false, length = 20)
    private String other_operating_income;
	
	@Column(name = "excise_duty", nullable = false, length = 20)
    private String excise_duty;
	
	@Column(name = "raw_materials_imported", nullable = false, length = 20)
    private String raw_materials_imported;
	
	@Column(name = "raw_materials_indigeneous", nullable = false, length = 20)
    private Long raw_materials_indigeneous;
	
	@Column(name = "other_spares_imported", nullable = false, length = 20)
    private String other_spares_imported;
	
	@Column(name = "other_spares_indigeneous", nullable = false, length = 20)
    private String other_spares_indigeneous;
	
	@Column(name = "powerFuel", nullable = false, length = 20)
    private String powerFuel;
	
	@Column(name = "directLabour", nullable = false, length = 20)
    private String directLabour;
	
	@Column(name = "otherMfgExpenses", nullable = false, length = 20)
    private String otherMfgExpenses;
	
	@Column(name = "repairsAndMaintenance", nullable = false, length = 20)
    private Long repairsAndMaintenance;
	
	@Column(name = "depreciation", nullable = false, length = 20)
    private String depreciation;
	
	@Column(name = "openingStockInProcess", nullable = false, length = 20)
    private String openingStockInProcess;
	
	@Column(name = "closingStockInProcess", nullable = false, length = 20)
    private String closingStockInProcess;
	
	@Column(name = "openingStockOfFinishedGoods", nullable = false, length = 20)
    private String openingStockOfFinishedGoods;
	
	@Column(name = "closingStockOfFinishedGoods", nullable = false, length = 20)
    private String closingStockOfFinishedGoods;
	
	@OneToMany(mappedBy = "cmatwo")
    private List<DomesticSalesComissionBrokerage> domestic_sales_comission_brokerage = new ArrayList<DomesticSalesComissionBrokerage>();
	
	@OneToMany(mappedBy = "cmatwo")
    private List<DirectLabour> direct_labour = new ArrayList<DirectLabour>();
	
	@OneToMany(mappedBy = "cmatwo")
    private List<ExciseDuty> exciseDuty = new ArrayList<ExciseDuty>();
	
	@OneToMany(mappedBy = "cmatwo")
    private List<ExportSales> exportSales = new ArrayList<ExportSales>();
	
	@OneToMany(mappedBy = "cmatwo")
    private List<OtherOperatingIncome> otherOperatingIncome = new ArrayList<OtherOperatingIncome>();
	
	@OneToMany(mappedBy = "cmatwo")
    private List<OtherSparesImported> otherSparesImported = new ArrayList<OtherSparesImported>();
	
	@OneToMany(mappedBy = "cmatwo")
    private List<OtherSparesIndigenious> otherSparesIndigenious = new ArrayList<OtherSparesIndigenious>();
	
	@OneToMany(mappedBy = "cmatwo")
    private List<PowerFuel> power_fuel = new ArrayList<PowerFuel>();
	
	@OneToMany(mappedBy = "cmatwo")
    private List<RawMaterialsImported> rawMaterialsImported = new ArrayList<RawMaterialsImported>();
	
	@OneToMany(mappedBy = "cmatwo")
    private List<RawMaterialsIndigenious> rawMaterialsIndigenious = new ArrayList<RawMaterialsIndigenious>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCma_one_id() {
		return cma_one_id;
	}

	public void setCma_one_id(Long cma_one_id) {
		this.cma_one_id = cma_one_id;
	}

	public String getDomestic_sales_commission_brokerage() {
		return domestic_sales_commission_brokerage;
	}

	public void setDomestic_sales_commission_brokerage(String domestic_sales_commission_brokerage) {
		this.domestic_sales_commission_brokerage = domestic_sales_commission_brokerage;
	}

	public String getExport_sales() {
		return export_sales;
	}

	public void setExport_sales(String export_sales) {
		this.export_sales = export_sales;
	}

	public String getOther_operating_income() {
		return other_operating_income;
	}

	public void setOther_operating_income(String other_operating_income) {
		this.other_operating_income = other_operating_income;
	}

	public String getExcise_duty() {
		return excise_duty;
	}

	public void setExcise_duty(String excise_duty) {
		this.excise_duty = excise_duty;
	}

	public String getRaw_materials_imported() {
		return raw_materials_imported;
	}

	public void setRaw_materials_imported(String raw_materials_imported) {
		this.raw_materials_imported = raw_materials_imported;
	}

	public Long getRaw_materials_indigeneous() {
		return raw_materials_indigeneous;
	}

	public void setRaw_materials_indigeneous(Long raw_materials_indigeneous) {
		this.raw_materials_indigeneous = raw_materials_indigeneous;
	}

	public String getOther_spares_imported() {
		return other_spares_imported;
	}

	public void setOther_spares_imported(String other_spares_imported) {
		this.other_spares_imported = other_spares_imported;
	}

	public String getOther_spares_indigeneous() {
		return other_spares_indigeneous;
	}

	public void setOther_spares_indigeneous(String other_spares_indigeneous) {
		this.other_spares_indigeneous = other_spares_indigeneous;
	}

	public String getPowerFuel() {
		return powerFuel;
	}

	public void setPowerFuel(String powerFuel) {
		this.powerFuel = powerFuel;
	}

	public String getDirectLabour() {
		return directLabour;
	}

	public void setDirectLabour(String directLabour) {
		this.directLabour = directLabour;
	}

	public String getOtherMfgExpenses() {
		return otherMfgExpenses;
	}

	public void setOtherMfgExpenses(String otherMfgExpenses) {
		this.otherMfgExpenses = otherMfgExpenses;
	}

	public Long getRepairsAndMaintenance() {
		return repairsAndMaintenance;
	}

	public void setRepairsAndMaintenance(Long repairsAndMaintenance) {
		this.repairsAndMaintenance = repairsAndMaintenance;
	}

	public String getDepreciation() {
		return depreciation;
	}

	public void setDepreciation(String depreciation) {
		this.depreciation = depreciation;
	}

	public String getOpeningStockInProcess() {
		return openingStockInProcess;
	}

	public void setOpeningStockInProcess(String openingStockInProcess) {
		this.openingStockInProcess = openingStockInProcess;
	}

	public String getClosingStockInProcess() {
		return closingStockInProcess;
	}

	public void setClosingStockInProcess(String closingStockInProcess) {
		this.closingStockInProcess = closingStockInProcess;
	}

	public String getOpeningStockOfFinishedGoods() {
		return openingStockOfFinishedGoods;
	}

	public void setOpeningStockOfFinishedGoods(String openingStockOfFinishedGoods) {
		this.openingStockOfFinishedGoods = openingStockOfFinishedGoods;
	}

	public String getClosingStockOfFinishedGoods() {
		return closingStockOfFinishedGoods;
	}

	public void setClosingStockOfFinishedGoods(String closingStockOfFinishedGoods) {
		this.closingStockOfFinishedGoods = closingStockOfFinishedGoods;
	}

	public CmaTwo(Long id, Long cma_one_id, String domestic_sales_commission_brokerage, String export_sales,
			String other_operating_income, String excise_duty, String raw_materials_imported,
			Long raw_materials_indigeneous, String other_spares_imported, String other_spares_indigeneous,
			String powerFuel, String directLabour, String otherMfgExpenses, Long repairsAndMaintenance,
			String depreciation, String openingStockInProcess, String closingStockInProcess,
			String openingStockOfFinishedGoods, String closingStockOfFinishedGoods) {
		super();
		this.id = id;
		this.cma_one_id = cma_one_id;
		this.domestic_sales_commission_brokerage = domestic_sales_commission_brokerage;
		this.export_sales = export_sales;
		this.other_operating_income = other_operating_income;
		this.excise_duty = excise_duty;
		this.raw_materials_imported = raw_materials_imported;
		this.raw_materials_indigeneous = raw_materials_indigeneous;
		this.other_spares_imported = other_spares_imported;
		this.other_spares_indigeneous = other_spares_indigeneous;
		this.powerFuel = powerFuel;
		this.directLabour = directLabour;
		this.otherMfgExpenses = otherMfgExpenses;
		this.repairsAndMaintenance = repairsAndMaintenance;
		this.depreciation = depreciation;
		this.openingStockInProcess = openingStockInProcess;
		this.closingStockInProcess = closingStockInProcess;
		this.openingStockOfFinishedGoods = openingStockOfFinishedGoods;
		this.closingStockOfFinishedGoods = closingStockOfFinishedGoods;
	}

	
	public CmaTwo() {
		// TODO Auto-generated constructor stub
	}
}

