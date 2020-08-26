package com.godmk.tool.easyreport.write;

import com.alibaba.excel.write.handler.AbstractSheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.godmk.tool.easyreport.constant.ExcelConstant;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author crazy22
 */
public class MergeStrategy extends AbstractSheetWriteHandler {
    private List<CellRangeAddress> cellRangeAddressList = new ArrayList<>();

    public MergeStrategy(List<Map> mergeList) {
        for (Map mergeMap : mergeList) {
            int sc = Integer.parseInt(mergeMap.get(ExcelConstant.START_COL).toString());
            int sr = Integer.parseInt(mergeMap.get(ExcelConstant.START_ROW).toString());
            int ec = Integer.parseInt(mergeMap.get(ExcelConstant.END_COL).toString());
            int er = Integer.parseInt(mergeMap.get(ExcelConstant.END_ROW).toString());
            if (sc < 0 || sr < 0 || ec < 0 || er < 0) {
                throw new IllegalArgumentException("All parameters must be greater than 0");
            }
            cellRangeAddressList.add(new CellRangeAddress(sr, er, sc, ec));
        }

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        if (cellRangeAddressList != null && cellRangeAddressList.size() > 0) {
            for (CellRangeAddress cellRangeAddress : cellRangeAddressList) {
                writeSheetHolder.getSheet().addMergedRegionUnsafe(cellRangeAddress);
            }
        }
    }
}
