import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.jedigames.dao.base.DaoException;
import com.jedigames.dao.database.base.DBRecord;
import com.jedigames.dao.database.mysql.MysqlDatabaseArrayObject;
import com.jedigames.dao.map.SysMapDao;
import com.jedigames.utils.JediRandom;
import com.jedigames.utils.TimeManager;

import Activities.ActStep;
import Activities.Activity;
import Activities.ActivityStatistician;
import Battle2.BattHero;
import Database.MainDaoManager;
import SceneMap.Manager.SceneMapMgr;
import Service.BattleNPC;
import Service.Equip;
import Service.Item;
import Service.Maps;
import Service.Rate;
import ServiceConstant.TechTypeConst;
import cmd.CmdJSONArray;
import dao.user.api.UEquipDao;
import dao.user.api.UGemDao;
import dao.user.api.UInfoDao;
import dao.user.api.UserActDao;
import game.config.manager.CfgAct109Manager;
import game.config.manager.CfgEquipManager;
import game.config.manager.CfgGemManager;
import game.config.tables.CfgAct109;
import game.config.tables.CfgEquip;
import game.config.tables.CfgGem;
import gameserver.protocol.message.ResGetAct814InfoMessage;
import main.ASTGenerator;
import main.ASTtoDOT;
import structure.MyASTNode;
import structure.MyMethodNode;
import task2.ActionBuilder;
import task2.action.ActionGemLevelCount;
import task2.action.ActionGemPutonCount;
import task2.action.ActionOpenGem;
import util.ExcelUtils;
import util.FileUtil;
import util.Graph;
import util.GraphOperations;
import util.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TryStatement;

import structure.MyASTNode;

public class HelloWorld {


	public boolean visit(EnhancedForStatement n) {
		for(int a:alist){
			int b=a;
		}
	}


}
