package com.dictionary.utils;

/**
 * @author Ihar Zharykau
 */
public class PreSetWords {
//    private final DictFactory DF = new DictFactory();
//
//    public void invokePreSet(Resource resource){
//        try {
//            File inputFile = resource.getFile();
//            DocumentBuilderFactory factory
//                    = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = factory.newDocumentBuilder();
//            Document doc = dBuilder.parse(inputFile);
//            doc.getDocumentElement().normalize();
//            nodeForeach(doc);
//        }
//        catch (IOException | SAXException | ParserConfigurationException e){
//            System.out.println(e);
//        }
//    }
//
//    private void nodeForeach(Document document){
//        NodeList wordList = document.getElementsByTagName("word");
//        for(int i = 0; i < wordList.getLength(); i++){
//            Node wordNode = wordList.item(i);
//            NodeList forms = wordNode.getChildNodes();
//            Set<String> strForms = new HashSet<>();
//            Set<String> strCodes = new HashSet<>();
//            for(int j = 0; j < forms.getLength(); j++){
//                if ( forms.item(j).getNodeName().equals("form")){
//                    strForms.add(forms.item(j).getTextContent());
//                } else if ( forms.item(j).getNodeName().equals("code")){
//                    strCodes.add(forms.item(j).getTextContent());
//                }
//            }
//            Set<PosTag> codes = new HashSet<>();
//            for(String strCode : strCodes){
//                PosTag lc = PosUtilsDB.byName(strCode);
//                if ( lc != null){
//                    codes.add(lc);
//                }
//            }
//            for(String form : strForms){
//                DictWord word = DictFactory.cacheMap.get(form.toLowerCase());
//                if ( word != null){
//                    word.getLobCodes().addAll(codes);
//                }
//            }
//        }
//    }
}
