package com.java.jacoco.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

	public static void test(String name, List<LinkedList<Vertex>> paths, Map<Integer, String> labelstring,
			String params) {
		// 第一步，创建一个workbook对应一个excel文件
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 第二部，在workbook中创建一个sheet对应excel中的sheet
		HSSFSheet sheet = workbook.createSheet(name);
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		// 第四部 设置列宽
		sheet.setDefaultColumnWidth(20);
		// 第三部，在sheet表中添加表头第0行，老版本的poi对sheet的行列有限制
		HSSFRow row = sheet.createRow(0);
		// 第四步，创建单元格，设置表头
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(params);
		HSSFCell cell2 = row.createCell(1);
		cell2.setCellValue("结果");
		cellStyle.setFillPattern(HSSFCellStyle.NO_FILL);
		cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index); //
		// 设置样式
		cell.setCellStyle(cellStyle);

		// cell = row.createCell(1);
		// cell.setCellValue("密码");

		// 第五步，写入实体数据，实际应用中这些数据从数据库得到,对象封装数据，集合包对象。对象的属性值对应表的每行的值

		for (int i = 0; i <paths.size() ; i++) {
			HSSFRow row1 = sheet.createRow(i + 1);
			LinkedList<Vertex> user = paths.get(i);
			// 创建单元格设值
			for (int j = 0; j < user.size(); j++) {
				String hashcode = user.get(j).getVertexName();
				HSSFCell littlecell = row1.createCell(j + 2);
				// + user.get(j).getVertexName()
				cellStyle.setWrapText(true); // 设置自动换行
				cellStyle = workbook.createCellStyle();// 每次都要重新创建
				cellStyle.setFillPattern(HSSFCellStyle.NO_FILL);
				String branchString = labelstring.get(Integer.valueOf(hashcode));
				if (branchString != null) {
					// 设置填充顔色
					if (branchString.equals("Yes")) {
						cellStyle.setFillForegroundColor(HSSFColor.GREEN.index); // 填绿色

					} else {
						cellStyle.setFillForegroundColor(HSSFColor.RED.index); // 填红色
					}
					cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); // 填充单元格
					
					littlecell.setCellValue("[" + labelstring.get(Integer.valueOf(hashcode)) + "]->" + "\r\n"
							+ user.get(j).getVertexCode());
				} else {
				
//					littlecell.setCellValue("->w" + "\r\n" + user.get(j).getVertexCode());
					littlecell.setCellValue("->pass" + "\r\n");
				}

				littlecell.setCellStyle(cellStyle);

			}
		}

		File file = new File("D:/whiteTest/Java-to-AST-with-Visualization-master318/output/" + name + "_"
				+ System.currentTimeMillis() + ".xls");
		if (file.exists()) {
			file.delete();
		}
		// 将文件保存到指定的位置
		try {
			file.createNewFile();
			workbook.write(file);
			System.out.println("写入成功");
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void testTxt(String name, List<LinkedList<Vertex>> paths, Map<Integer, String> labelstring,
			String params) {
		String code = "total TestNum："+paths.size();
		for (int i =0; i <paths.size() ; i++) {
			code += "\n\n\n[TEST"+(i)+"] "+name+"("+params+")"+"\n";
			LinkedList<Vertex> user = paths.get(i);
			// 创建单元格设值
			for (int j = 0; j < user.size(); j++) {
				String hashcode = user.get(j).getVertexName();

				String branchString = labelstring.get(Integer.valueOf(hashcode));
				if (branchString != null) {
					// 设置填充顔色
					if (branchString.equals("Yes")) {

					} else {

					}
					code += "[" + labelstring.get(Integer.valueOf(hashcode)) + "]->" + "\r\n"
							+ user.get(j).getVertexCode()+ "\r\n";

				} else {
					code += user.get(j).getVertexCode() + "\r\n";
				}

			}
		}

		// 将文件保存到指定的位置
		try {
			FileUtil.writeFile("D:/whiteTest/Java-to-AST-with-Visualization-master318/output/" + name + "_"
					+ System.currentTimeMillis() + ".txt", code);
			System.out.println("写入成功");

		} catch (IOException e) {
			e.printStackTrace();
		}

		// ————————————————
		// 版权声明：本文为CSDN博主「帅帅的光哥」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
		// 原文链接：https://blog.csdn.net/hnnydxhxg/article/details/98084931
		// 版权声明：本文为CSDN博主「linge__」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
		// 原文链接：https://blog.csdn.net/weixin_37497666/article/details/78921462
	}
	  /**
     * @description 将数据归档到.doc的word文档中。数据续写到原目标文件末尾。
     * @param source
     *            源文件（必须存在！）
     * @param sourChs
     *            读取源文件要用的编码，若传入null，则默认是GBK编码
     * @param target
     *            目标word文档（必须存在！）
     */
//    public static void storeDoc(File source, String sourChs, File target) {
//        /*
//         * 思路： 1.建立字符输入流，读取source中的数据。 2.在目标文件路径下new File：temp.doc
//         * 3.将目标文件重命名为temp.doc，并用HWPFDocument类关联（temp.doc）。
//         * 3.由temp.doc建立Range对象，写入source中的数据。 4.建立字节输出流，关联target。
//         * 5.将range中的数据写入关联target的字节输出流。
//         */
//        if (!target.exists()) {
//            throw new RuntimeException("目标文件不存在！");
//        }
//        if (sourChs == null) {
//            sourChs = "GBK";
//        }
//        BufferedReader in = null;
//        HWPFDocument temp = null;
//        BufferedOutputStream out = null;
//        String path = target.getParent();
//        File tempDoc = new File(path, "temp.doc");
//        target.renameTo(tempDoc);
//        try {
//            in = new BufferedReader(new InputStreamReader(new FileInputStream(source), sourChs));
//            temp = new HWPFDocument(new BufferedInputStream(new FileInputStream(tempDoc)));
//            out = new BufferedOutputStream(new FileOutputStream(target));
//            Range range = temp.getRange();
//            String line = null;
//            range.insertAfter(getDate(12));
//            range.insertAfter("\r");
//            while ((line = in.readLine()) != null) {
//                range.insertAfter(line);
//                range.insertAfter("\r"); // word中\r是换行符
//            }
//            range.insertAfter("\r");
//            range.insertAfter("\r");
//            temp.write(out);
//        } catch (UnsupportedEncodingException e) {
//            // TODO 自动生成的 catch 块
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            // TODO 自动生成的 catch 块
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO 自动生成的 catch 块
//            e.printStackTrace();
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    // TODO 自动生成的 catch 块
//                    e.printStackTrace();
//                }
//            }
//            if (temp != null) {
//                try {
//                    temp.close();
//                } catch (IOException e) {
//                    // TODO 自动生成的 catch 块
//                    e.printStackTrace();
//                }
//            }
//            if (out != null) {
//                try {
//                    out.close();
//                } catch (IOException e) {
//                    // TODO 自动生成的 catch 块
//                    e.printStackTrace();
//                }
//            }
//            tempDoc.deleteOnExit();
//        }
//    }
}
