import org.xml.sax.helpers.AttributesImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

public class Main {
    //    public static String path = "./libdimens/src/main/res/values/";
    public static String path = "./";
    public static String fileName = "dimens.xml";
    public static float DP_FROM = -2560f;
    public static float DP_TO = 2560f;
    public static float DP_STEP_SIZE = 0.5f;
    public static float SP_FROM = 9f;
    public static float SP_TO = 30f;
    public static float SP_STEP_SIZE = 1f;

    public static void main(String[] args) {
        config(args);
        System.out.println("dp from:" + DP_FROM + ",to:" + DP_TO + ",step:" + DP_STEP_SIZE);
        System.out.println("sp from:" + SP_FROM + ",to:" + SP_TO + " ,step:" + SP_STEP_SIZE);
        createDimens();
    }

    /**
     * 配置参数
     *
     * @param args
     */
    private static void config(String[] args) {
        if (args == null || args.length < 1) {
            return;
        }
        try {
            DP_FROM = Float.parseFloat(args[0]);
        } catch (Exception e) {
        }
        try {
            DP_TO = Float.parseFloat(args[1]);
        } catch (Exception e) {
        }
        try {
            DP_STEP_SIZE = Float.parseFloat(args[2]);
        } catch (Exception e) {
        }
        try {
            SP_FROM = Float.parseFloat(args[3]);
        } catch (Exception e) {
        }
        try {
            SP_TO = Float.parseFloat(args[4]);
        } catch (Exception e) {
        }
        try {
            SP_STEP_SIZE = Float.parseFloat(args[5]);
        } catch (Exception e) {
        }
    }

    private static void createDimens() {
        List<DimenItem> items = new ArrayList<>();
        items.addAll(cerateDpData());
        items.addAll(cerateSpData());
        createDestinationDimens(items, path + fileName);
    }

    /**
     * 生成dp数据
     *
     * @return
     */
    private static List<DimenItem> cerateDpData() {
        List<DimenItem> items = new ArrayList<>();
        float dp = DP_FROM;
        String dpName;
        while (dp >= DP_FROM && dp <= DP_TO) {
            if (dp == 0) {
                dp += DP_STEP_SIZE;
                continue;
            }
            if (dp > 0) {
                dpName = String.valueOf(dp).replace(".", "_");
            } else {
                dpName = "m_" + String.valueOf(Math.abs(dp)).replace(".", "_");
            }
            if (dpName.endsWith("_0")) {
                dpName = dpName.substring(0, dpName.length() - 2);
            }
            DimenItem dimenItem = new DimenItem();
            dimenItem.name = "dp_" + dpName;
            dimenItem.value = dp + "dp";
            items.add(dimenItem);
            dp += DP_STEP_SIZE;
        }
        return items;
    }

    /**
     * 生成sp数据
     *
     * @return
     */
    private static List<DimenItem> cerateSpData() {
        List<DimenItem> items = new ArrayList<>();
        float sp = SP_FROM;
        String spName;
        while (sp >= SP_FROM && sp <= SP_TO) {
            spName = String.valueOf(sp).replace(".", "_");
            if (spName.endsWith("_0")) {
                spName = spName.substring(0, spName.length() - 2);
            }
            DimenItem dimenItem = new DimenItem();
            dimenItem.name = "sp_" + spName;
            dimenItem.value = sp + "sp";
            items.add(dimenItem);
            sp += SP_STEP_SIZE;
        }
        return items;
    }

    /**
     * 输出dimens.xml
     *
     * @param list
     * @param outPutFile
     */
    private static void createDestinationDimens(List<DimenItem> list, String outPutFile) {
        System.out.println(">>>>> " + outPutFile + " 开始生成文件");
        try {
            File targetFile = new File(outPutFile);
            if (targetFile.exists()) {
                try {
                    targetFile.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //创建SAXTransformerFactory实例
            SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            //创建TransformerHandler实例
            TransformerHandler handler = saxTransformerFactory.newTransformerHandler();
            //创建Transformer实例
            Transformer transformer = handler.getTransformer();
            //是否自动添加额外的空白
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //设置字符编码
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            //添加xml版本，默认也是1.0
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
            //保存xml路径
            StreamResult result = new StreamResult(targetFile);
            handler.setResult(result);
            //创建属性Attribute对象
            AttributesImpl attributes = new AttributesImpl();
            attributes.clear();
            //开始xml
            handler.startDocument();
            //换行
            handler.characters("\n".toCharArray(), 0, "\n".length());
            //写入根节点resources
            handler.startElement("", "", SAXReadHandler.ELEMENT_RESOURCE, attributes);
            //集合大小
            int size = list.size();
            for (int i = 0; i < size; i++) {
                DimenItem dimenBean = list.get(i);
                String value = dimenBean.value;
                attributes.clear();
                attributes.addAttribute("", "", SAXReadHandler.PROPERTY_NAME, "", dimenBean.name);

                //新dimen之前，换行、缩进
                handler.characters("\n".toCharArray(), 0, "\n".length());
                handler.characters("\t".toCharArray(), 0, "\t".length());

                //开始标签对输出
                handler.startElement("", "", SAXReadHandler.ELEMENT_DIMEN, attributes);
                handler.characters(value.toCharArray(), 0, value.length());
                handler.endElement("", "", SAXReadHandler.ELEMENT_DIMEN);
            }
            handler.endElement("", "", SAXReadHandler.ELEMENT_RESOURCE);
            handler.endDocument();
            System.out.println(">>>>> " + outPutFile + " 文件生成完成!");
        } catch (Exception e) {
            System.out.println("WARNING: " + outPutFile + " 文件生成失败!");
            e.printStackTrace();
        }
    }

}
