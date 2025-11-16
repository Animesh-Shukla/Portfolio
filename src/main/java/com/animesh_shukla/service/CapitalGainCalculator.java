package com.animesh_shukla.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CapitalGainCalculator {
	private static final Logger logger = LoggerFactory.getLogger(CapitalGainCalculator.class);

	public static class Entry {
		public String stockSymbol;
		public String isin;
		public int qty;
		public String saleDate;
		public double saleRate;
		public double saleValue;
		public String purchaseDate;
		public double purchaseRate;
		public double purchaseValue;
		public double profitLoss;
	}

	public List<Entry> parseFile(File file) throws IOException {
		String name = file.getName().toLowerCase();
		logger.info("Parsing file: {}", name);
		if (name.endsWith(".csv")) {
			logger.info("Detected CSV file");
			return parseCsv(file);
		} else if (name.endsWith(".xls") || name.endsWith(".xlsx")) {
			logger.info("Detected Excel file");
			return parseExcel(file);
		} else {
			logger.error("Unsupported file type: {}", name);
			throw new IllegalArgumentException("Unsupported file type: " + name);
		}
	}

	private List<Entry> parseCsv(File file) throws IOException {
		List<Entry> entries = new ArrayList<>();
		logger.info("Starting CSV parsing: {}", file.getName());
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String header = br.readLine(); // skip header
			logger.debug("CSV header: {}", header);
			String line;
			int lineNum = 1;
			while ((line = br.readLine()) != null) {
				lineNum++;
				String[] cols = line.split(",|\t");
				if (cols.length < 10) {
					logger.warn("Line {} skipped, not enough columns: {}", lineNum, line);
					continue;
				}
				Entry e = new Entry();
				e.stockSymbol = cols[0].trim();
				e.isin = cols[1].trim();
				e.qty = parseInt(cols[2]);
				e.saleDate = cols[3].trim();
				e.saleRate = parseDouble(cols[4]);
				e.saleValue = parseDouble(cols[5]);
				e.purchaseDate = cols[6].trim();
				e.purchaseRate = parseDouble(cols[7]);
				e.purchaseValue = parseDouble(cols[8]);
				e.profitLoss = parseDouble(cols[9]);
				entries.add(e);
				logger.debug("Parsed CSV entry: {}", e);
			}
		} catch (Exception ex) {
			logger.error("Error parsing CSV file: {}", ex.getMessage(), ex);
			throw ex;
		}
		logger.info("CSV parsing complete. Parsed {} entries.", entries.size());
		return entries;
	}

	private List<Entry> parseExcel(File file) throws IOException {
		List<Entry> entries = new ArrayList<>();
		logger.info("Starting Excel parsing: {}", file.getName());
		try (InputStream is = new FileInputStream(file)) {
			Workbook workbook = file.getName().endsWith(".xlsx") ? new XSSFWorkbook(is) : new HSSFWorkbook(is);
			Sheet sheet = workbook.getSheetAt(0);
			boolean firstRow = true;
			int rowNum = 0;
			for (Row row : sheet) {
				rowNum++;
				if (firstRow) { firstRow = false; logger.debug("Excel header row: {}", rowNum); continue; } // skip header
				if (row.getPhysicalNumberOfCells() < 10) {
					logger.warn("Row {} skipped, not enough columns.", rowNum);
					continue;
				}
				Entry e = new Entry();
				e.stockSymbol = getCellString(row.getCell(0));
				e.isin = getCellString(row.getCell(1));
				e.qty = (int) getCellNumeric(row.getCell(2));
				e.saleDate = getCellString(row.getCell(3));
				e.saleRate = getCellNumeric(row.getCell(4));
				e.saleValue = getCellNumeric(row.getCell(5));
				e.purchaseDate = getCellString(row.getCell(6));
				e.purchaseRate = getCellNumeric(row.getCell(7));
				e.purchaseValue = getCellNumeric(row.getCell(8));
				e.profitLoss = getCellNumeric(row.getCell(9));
				entries.add(e);
				logger.debug("Parsed Excel entry: {}", e);
			}
			workbook.close();
		} catch (Exception ex) {
			logger.error("Error parsing Excel file: {}", ex.getMessage(), ex);
			throw ex;
		}
		logger.info("Excel parsing complete. Parsed {} entries.", entries.size());
		return entries;
	}

	private int parseInt(String s) {
		try { return Integer.parseInt(s.trim()); } catch (Exception e) {
			logger.warn("Failed to parse int from '{}': {}", s, e.getMessage());
			return 0;
		}
	}
	private double parseDouble(String s) {
		try { return Double.parseDouble(s.trim()); } catch (Exception e) {
			logger.warn("Failed to parse double from '{}': {}", s, e.getMessage());
			return 0.0;
		}
	}
	private String getCellString(Cell cell) {
		if (cell == null) return "";
		if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
		if (cell.getCellType() == CellType.NUMERIC) return String.valueOf(cell.getNumericCellValue());
		return "";
	}
	private double getCellNumeric(Cell cell) {
		if (cell == null) return 0.0;
		if (cell.getCellType() == CellType.NUMERIC) return cell.getNumericCellValue();
		if (cell.getCellType() == CellType.STRING) return parseDouble(cell.getStringCellValue());
		return 0.0;
	}
}
