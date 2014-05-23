package com.royalstone.pos.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRootPane;

public class HelloHelp {
  public static void main(String args[]) {
    JFrame frame = new JFrame("Hello, JavaHelp");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container content = frame.getContentPane();

    JMenuBar menubar = new JMenuBar();
    JMenu helpMenu = new JMenu("Help");
    JMenuItem overview = new JMenuItem("Overview");
    JMenuItem specific = new JMenuItem("Specific");
    helpMenu.add(overview);
    helpMenu.add(specific);
    menubar.add(helpMenu);
    frame.setJMenuBar(menubar);

    JButton button1 = new JButton("The Button");
    JButton button2 = new JButton("Context");

    content.add(button1, BorderLayout.NORTH);
    content.add(button2, BorderLayout.SOUTH);

    HelpSet helpset = null;
    ClassLoader loader = null;
    URL url = HelpSet.findHelpSet(loader, "help/hello.hs");
    try {
      helpset = new HelpSet(loader, url);
    } catch (HelpSetException e) {
      System.err.println("Error loading");
      return;
    }

    HelpBroker helpbroker = helpset.createHelpBroker();

    ActionListener listener = 
      new CSH.DisplayHelpFromSource(helpbroker);
    overview.addActionListener(listener);

    CSH.setHelpIDString(specific, "one");
    specific.addActionListener(listener);

    CSH.setHelpIDString(button1, "two");
    ActionListener tracker = 
      new CSH.DisplayHelpAfterTracking(helpbroker);
    button2.addActionListener(tracker);

    JRootPane rootpane = frame.getRootPane();
    helpbroker.enableHelpKey(rootpane, "three", helpset);

    frame.setSize(200, 200);
    frame.show();
  }
}