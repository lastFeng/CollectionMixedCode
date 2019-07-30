/*
 * Copyright 2001-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xml;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/7/23 16:50
 */
public class XPathTest {
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        // set setting
        documentBuilderFactory.setValidating(true);
        documentBuilderFactory.setNamespaceAware(false);
        documentBuilderFactory.setIgnoringComments(true);
        documentBuilderFactory.setIgnoringElementContentWhitespace(false);
        documentBuilderFactory.setCoalescing(false);
        documentBuilderFactory.setExpandEntityReferences(true);

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        documentBuilder.setErrorHandler(new ErrorHandler() {
            public void warning(SAXParseException exception) throws SAXException {
                System.out.println("WARN: " + exception.getMessage());
            }

            public void error(SAXParseException exception) throws SAXException {
                System.out.println("Error: " + exception.getMessage());
            }

            public void fatalError(SAXParseException exception) throws SAXException {
                System.out.println("Fatal: " + exception.getMessage());
            }
        });

        Document document = documentBuilder.parse("src/main/java/xml/inventory.xml");
        // 创建XPathFactory
        XPathFactory factory = XPathFactory.newInstance();
        // 创建XPath对象
        XPath xPath = factory.newXPath();
        // 编译XPath表达式
        String expr = "//book[author='Neal Stephenson']/title/text()";
        XPathExpression expression = xPath.compile(expr);

        // 返回结果 第一个参数：需要搜索的范围，第二个参数：返回类型
        Object result = expression.evaluate(document, XPathConstants.NODESET);
        System.out.println("查找到的标题：");

        NodeList nodeList = (NodeList) result;
        for (int i = 0; i < nodeList.getLength(); i++){
            System.out.println(nodeList.item(i).getNodeValue());
        }
    }
}