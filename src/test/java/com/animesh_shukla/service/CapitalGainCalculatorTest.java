package com.animesh_shukla.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.io.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class CapitalGainCalculatorTest {
    private CapitalGainCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new CapitalGainCalculator();
    }

    @Test
    void testParseCsvAndFilterBySaleDate_yyyyMMdd() throws IOException {
        File temp = File.createTempFile("test", ".csv");
        try (PrintWriter pw = new PrintWriter(temp)) {
            pw.println("Stock Symbol,ISIN,Qty,Sale Date,Sale Rate,Sale Value,Purchase Date,Purchase Rate,Purchase Value,Profit/Loss");
            pw.println("TCS,IN123,10,2025-10-14,100,1000,2025-01-01,90,900,100");
            pw.println("INFY,IN456,5,2025-10-15,200,1000,2025-01-01,180,900,100");
            pw.println("WIPRO,IN789,8,2025-10-13,150,1200,2025-01-01,140,1120,80");
        }
        List<CapitalGainCalculator.Entry> entries = calculator.parseFile(temp);
        assertEquals(3, entries.size());
        List<CapitalGainCalculator.Entry> filtered = calculator.filterBySaleDate(entries, "2025-10-14", "2025-10-15");
        assertEquals(2, filtered.size());
        assertTrue(filtered.stream().anyMatch(e -> e.stockSymbol.equals("TCS")));
        assertTrue(filtered.stream().anyMatch(e -> e.stockSymbol.equals("INFY")));
        temp.delete();
    }

    @Test
    void testCalculateFilteredPnL() throws IOException {
        File temp = File.createTempFile("test", ".csv");
        try (PrintWriter pw = new PrintWriter(temp)) {
            pw.println("Stock Symbol,ISIN,Qty,Sale Date,Sale Rate,Sale Value,Purchase Date,Purchase Rate,Purchase Value,Profit/Loss");
            pw.println("TCS,IN123,10,2025-10-14,100,1000,2025-01-01,90,900,100");
            pw.println("INFY,IN456,5,2025-10-15,200,1000,2025-01-01,180,900,200");
            pw.println("WIPRO,IN789,8,2025-10-13,150,1200,2025-01-01,140,1120,80");
        }
        double pnl = calculator.calculateFilteredPnL(temp, "2025-10-14", "2025-10-15");
        assertEquals(300, pnl, 0.001); // 100 + 200
        temp.delete();
    }

    @Test
    void testFilterBySaleDate_invalidDate() {
        List<CapitalGainCalculator.Entry> entries = new ArrayList<>();
        CapitalGainCalculator.Entry e = new CapitalGainCalculator.Entry();
        e.saleDate = "invalid-date";
        entries.add(e);
        List<CapitalGainCalculator.Entry> filtered = calculator.filterBySaleDate(entries, "2025-10-14", "2025-10-15");
        assertEquals(0, filtered.size());
    }
}
