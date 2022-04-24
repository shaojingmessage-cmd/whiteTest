package Main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.java.jacoco.ASTDotGenerator;
import com.java.jacoco.ASTGenerator;
import com.java.jacoco.ASTtoDOT;
import com.java.jacoco.structure.MyMethodNode;
import com.java.jacoco.util.FileUtil;

public class Main {

	/**
	 * given the path of a java program which you want to parse and the output
	 * directory
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// 获得文件
		 String FilePath = "testdata2/";
		//D:/p16/devel/server/jedigames-parent/server-subserver-main/src/Service/
//		String FilePath = "D:/p16/devel/server/jedigames-parent/server-subserver-main/src/Service/";
		String outputDir = "output/";
		String outputBatDir = "outputBat/";

		List<String> fileList = FileUtil.getAllFile(FilePath, false);
		for (String dir : fileList) {

			File f = new File(dir);

			ASTDotGenerator astGenerator = new ASTDotGenerator(f);
			List<MyMethodNode> methodNodeList = astGenerator.getMethodNodeList();
			String str = "";
			FileUtil.mkDir("outputBat");
			String fileName = outputBatDir + f.getName() + "_" + "file";
			FileUtil.mkDir(fileName);
			String fileOwn = outputBatDir + f.getName() + "_" + "file/";
			System.out.println("/****************"+f.getName()+"***********************/");
			for (MyMethodNode m : methodNodeList) {
				String dotStr = ASTtoDOT.ASTtoDotParser(m);
				FileUtil.writeFile(fileOwn + f.getName() + "_" + m.methodNode.getName() + ".dot", dotStr);
				str += ("dot -Tpng " + f.getName() + "_" + m.methodNode.getName() + ".dot" + " -o " + f.getName() + "_"
						+ m.methodNode.getName() + ".png" + "\n");
			}
			FileUtil.writeFile(fileOwn + f.getName() + "_" + "png" + ".bat", str);
		}

		System.out.println("Done.");
	}

}
