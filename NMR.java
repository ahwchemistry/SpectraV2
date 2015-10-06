//Major thanks to Gunther on StackOverflow for how to import XML in Java
//Major thanks to GK27 on StackOverflow for how to get attributes from XML code


import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xpath.*;
import javax.swing.JFrame;
import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.util.Stack;

public class NMR {

    public String inchi;
    public String accessurl;
    public String spectraselect;
    public NodeList xpoints;

    //Precondition- Valid Smiles Code, Spectra Type (13C, 1H)
    public NMR(String inchicode, String SpectraType) {
        inchi = inchicode;

        spectraselect = SpectraType;

        InchIEncoder accessinchi = new InchIEncoder(inchi);
        
        

        accessurl = "http://nmrshiftdb.nmr.uni-koeln.de/NmrshiftdbServlet/nmrshiftdbaction/exportcmlbyinchi/inchi/" + accessinchi.Encode() + "/spectrumtype/" + spectraselect;

        System.out.println(accessurl);

        try {
            xpoints = SpectraLookup.lookup(accessurl);
        }
        catch(Exception e)
        {
            System.out.println("Invalid InchI Key.");
        }
    }

    public NodeList getpoints()
    {
        return xpoints;
    }



    public static void main(String[] args) {

        Stack<Double> xvalues = new Stack<Double>();

        NMR mynmr = new NMR("InChI=1S/C9H8O2/c10-9(11)7-6-8-4-2-1-3-5-8/h1-7H,(H,10,11)", "1H");

        NodeList signals = mynmr.getpoints();


        for (int i = 0; i < signals.getLength(); i++) {
            Node currentItem = signals.item(i);
            
            String key = currentItem.getAttributes().getNamedItem("xValue").getNodeValue();

            Double xvalue = Double.parseDouble(key);

            xvalues.add(xvalue);
        }
        
        Points points = new Points(xvalues);
        JFrame frame = new JFrame("NMR Spectrum");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(points);
        frame.setSize(1500, 1000);
        frame.setResizable(false);
        frame.setVisible(true);
         

    }

}


