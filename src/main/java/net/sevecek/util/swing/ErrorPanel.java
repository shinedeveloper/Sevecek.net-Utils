package net.sevecek.util.swing;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.border.*;

public class ErrorPanel extends JPanel {

    static Logger logger;       // Is initialized from SwingExceptionHandler

    private static final String ERROR_DIALOG_TITLE_KEY             = "SwingExceptionHandler->ErrorDialog->Title";
    private static final String SHOW_STACK_TRACE_CHECKBOX_KEY      = "SwingExceptionHandler->ErrorDialog->Checkbox->Show_stack_trace";
    private static final String SHOW_STACK_TRACE_CHECKBOX_MNEMONIC = "SwingExceptionHandler->ErrorDialog->Checkbox->Show_stack_trace->Mnemonic";
    private static final String BUTTON_OK_KEY                      = "SwingExceptionHandler->ErrorDialog->Button->OK";
    private static final String BUTTON_DETAILS_KEY                 = "SwingExceptionHandler->ErrorDialog->Button->Log_details";

/*
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        String messageString = "Exception in thread \"AWT-EventQueue-0\" org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'mainWindow' defined in class path resource [net/sevecek/videoboss/applicationContext.xml]: Cannot resolve reference to bean 'filmRepository' while setting constructor argument; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'filmRepository' defined in class path resource [net/sevecek/videoboss/applicationContext.xml]: Cannot resolve reference to bean 'webServiceFactory' while setting constructor argument; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'webServiceFactory' defined in class path resource [net/sevecek/videoboss/applicationContext.xml]: Instantiation of bean failed; nested exception is org.springframework.beans.factory.BeanDefinitionStoreException: Factory method [public static javax.xml.ws.Service javax.xml.ws.Service.create(java.net.URL,javax.xml.namespace.QName)] threw exception; nested exception is javax.xml.ws.WebServiceException: Failed to access the WSDL at: http://localhost:8080/70-Films_from_Java-Lab/SOAPFilmWebService?wsdl. It failed with:\nGot Connection refused: connect while opening stream from http://localhost:8080/70-Films_from_Java-Lab/SOAPFilmWebService?wsdl.";
        String stackTraceString = "Exception in thread \"AWT-EventQueue-0\" org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'mainWindow' defined in class path resource [net/sevecek/videoboss/applicationContext.xml]: Cannot resolve reference to bean 'filmRepository' while setting constructor argument; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'filmRepository' defined in class path resource [net/sevecek/videoboss/applicationContext.xml]: Cannot resolve reference to bean 'webServiceFactory' while setting constructor argument; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'webServiceFactory' defined in class path resource [net/sevecek/videoboss/applicationContext.xml]: Instantiation of bean failed; nested exception is org.springframework.beans.factory.BeanDefinitionStoreException: Factory method [public static javax.xml.ws.Service javax.xml.ws.Service.create(java.net.URL,javax.xml.namespace.QName)] threw exception; nested exception is javax.xml.ws.WebServiceException: Failed to access the WSDL at: http://localhost:8080/70-Films_from_Java-Lab/SOAPFilmWebService?wsdl. It failed with: Got Connection refused: connect while opening stream from http://localhost:8080/70-Films_from_Java-Lab/SOAPFilmWebService?wsdl.\n" +
                "\tat org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveReference(BeanDefinitionValueResolver.java:328)\n\tat org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveValueIfNecessary(BeanDefinitionValueResolver.java:106)\n\tat org.springframework.beans.factory.support.ConstructorResolver.resolveConstructorArguments(ConstructorResolver.java:630)\n\tat org.springframework.beans.factory.support.ConstructorResolver.autowireConstructor(ConstructorResolver.java:148)\n\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireConstructor(AbstractAutowireCapableBeanFactory.java:1035)\n\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:939)\n\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:485)\n\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:456)\n\tat org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:294)\n\tat org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:225)\n\tat org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:291)\n\tat org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:193)\n\tat org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:585)\n\tat org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:913)\n\tat org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:464)\n\tat org.springframework.context.support.ClassPathXmlApplicationContext.<init>(ClassPathXmlApplicationContext.java:139)\n\tat org.springframework.context.support.ClassPathXmlApplicationContext.<init>(ClassPathXmlApplicationContext.java:83)\n\tat net.sevecek.videoboss.Main.showMainWindow(Main.java:26)\n\tat net.sevecek.videoboss.Main.access$000(Main.java:12)\n\tat net.sevecek.videoboss.Main$1.run(Main.java:19)\n\tat java.awt.event.InvocationEvent.dispatch(InvocationEvent.java:251)\n\tat java.awt.EventQueue.dispatchEventImpl(EventQueue.java:727)\n\tat java.awt.EventQueue.access$200(EventQueue.java:103)\n\tat java.awt.EventQueue$3.run(EventQueue.java:688)\n\tat java.awt.EventQueue$3.run(EventQueue.java:686)\n\tat java.security.AccessController.doPrivileged(Native Method)\n\tat java.security.ProtectionDomain$1.doIntersectionPrivilege(ProtectionDomain.java:76)\n\tat java.awt.EventQueue.dispatchEvent(EventQueue.java:697)\n\tat java.awt.EventDispatchThread.pumpOneEventForFilters(EventDispatchThread.java:242)\n\tat java.awt.EventDispatchThread.pumpEventsForFilter(EventDispatchThread.java:161)\n\tat java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:150)\n\tat java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:146)\n\tat java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:138)\n\tat java.awt.EventDispatchThread.run(EventDispatchThread.java:91)\n" +
                "Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'filmRepository' defined in class path resource [net/sevecek/videoboss/applicationContext.xml]: Cannot resolve reference to bean 'webServiceFactory' while setting constructor argument; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'webServiceFactory' defined in class path resource [net/sevecek/videoboss/applicationContext.xml]: Instantiation of bean failed; nested exception is org.springframework.beans.factory.BeanDefinitionStoreException: Factory method [public static javax.xml.ws.Service javax.xml.ws.Service.create(java.net.URL,javax.xml.namespace.QName)] threw exception; nested exception is javax.xml.ws.WebServiceException: Failed to access the WSDL at: http://localhost:8080/70-Films_from_Java-Lab/SOAPFilmWebService?wsdl. It failed with: ntGot Connection refused: connect while opening stream from http://localhost:8080/70-Films_from_Java-Lab/SOAPFilmWebService?wsdl.\n\tat org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveReference(BeanDefinitionValueResolver.java:328)\n\tat org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveValueIfNecessary(BeanDefinitionValueResolver.java:106)\n\tat org.springframework.beans.factory.support.ConstructorResolver.resolveConstructorArguments(ConstructorResolver.java:630)\n\tat org.springframework.beans.factory.support.ConstructorResolver.autowireConstructor(ConstructorResolver.java:148)\n\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireConstructor(AbstractAutowireCapableBeanFactory.java:1035)\n\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:939)\n\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:485)\n\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:456)\n\tat org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:294)\n\tat org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:225)\n\tat org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:291)\n\tat org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:193)\n\tat org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveReference(BeanDefinitionValueResolver.java:322)nt... 33 more\n" +
                "Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'webServiceFactory' defined in class path resource [net/sevecek/videoboss/applicationContext.xml]: Instantiation of bean failed; nested exception is org.springframework.beans.factory.BeanDefinitionStoreException: Factory method [public static javax.xml.ws.Service javax.xml.ws.Service.create(java.net.URL,javax.xml.namespace.QName)] threw exception; nested exception is javax.xml.ws.WebServiceException: Failed to access the WSDL at: http://localhost:8080/70-Films_from_Java-Lab/SOAPFilmWebService?wsdl. It failed with: ntGot Connection refused: connect while opening stream from http://localhost:8080/70-Films_from_Java-Lab/SOAPFilmWebService?wsdl.\n\tat org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:581)\n\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1015)\n\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:911)\n\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:485)\n\tat org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:456)\n\tat org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:294)\n\tat org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:225)\n\tat org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:291)\n\tat org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:193)\n\tat org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveReference(BeanDefinitionValueResolver.java:322)nt... 45 more\n" +
                "Caused by: org.springframework.beans.factory.BeanDefinitionStoreException: Factory method [public static javax.xml.ws.Service javax.xml.ws.Service.create(java.net.URL,javax.xml.namespace.QName)] threw exception; nested exception is javax.xml.ws.WebServiceException: Failed to access the WSDL at: http://localhost:8080/70-Films_from_Java-Lab/SOAPFilmWebService?wsdl. It failed with: ntGot Connection refused: connect while opening stream from http://localhost:8080/70-Films_from_Java-Lab/SOAPFilmWebService?wsdl.\n\tat org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:169)\n\tat org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:570)nt... 54 more\n" +
                "Caused by: javax.xml.ws.WebServiceException: Failed to access the WSDL at: http://localhost:8080/70-Films_from_Java-Lab/SOAPFilmWebService?wsdl. It failed with: ntGot Connection refused: connect while opening stream from http://localhost:8080/70-Films_from_Java-Lab/SOAPFilmWebService?wsdl.\n\tat com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser.tryWithMex(RuntimeWSDLParser.java:173)\n\tat com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser.parse(RuntimeWSDLParser.java:155)\n\tat com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser.parse(RuntimeWSDLParser.java:120)\n\tat com.sun.xml.internal.ws.client.WSServiceDelegate.parseWSDL(WSServiceDelegate.java:257)\n\tat com.sun.xml.internal.ws.client.WSServiceDelegate.<init>(WSServiceDelegate.java:220)\n\tat com.sun.xml.internal.ws.client.WSServiceDelegate.<init>(WSServiceDelegate.java:168)\n\tat com.sun.xml.internal.ws.spi.ProviderImpl.createServiceDelegate(ProviderImpl.java:96)\n\tat javax.xml.ws.Service.<init>(Service.java:77)\n\tat javax.xml.ws.Service.create(Service.java:707)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.lang.reflect.Method.invoke(Method.java:601)\n\tat org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:149)nt... 55 more\n" +
                "Caused by: java.io.IOException: Got Connection refused: connect while opening stream from http://localhost:8080/70-Films_from_Java-Lab/SOAPFilmWebService?wsdl\n\tat com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser.createReader(RuntimeWSDLParser.java:842)\n\tat com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser.resolveWSDL(RuntimeWSDLParser.java:283)\n\tat com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser.parse(RuntimeWSDLParser.java:140)nt... 67 more\n" +
                "Caused by: java.net.ConnectException: Connection refused: connect\n\tat java.net.DualStackPlainSocketImpl.connect0(Native Method)\n\tat java.net.DualStackPlainSocketImpl.socketConnect(DualStackPlainSocketImpl.java:69)\n\tat java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:339)\n\tat java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:200)\n\tat java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:182)\n\tat java.net.PlainSocketImpl.connect(PlainSocketImpl.java:157)\n\tat java.net.SocksSocketImpl.connect(SocksSocketImpl.java:391)\n\tat java.net.Socket.connect(Socket.java:579)\n\tat java.net.Socket.connect(Socket.java:528)\n\tat sun.net.NetworkClient.doConnect(NetworkClient.java:180)\n\tat sun.net.www.http.HttpClient.openServer(HttpClient.java:378)\n\tat sun.net.www.http.HttpClient.openServer(HttpClient.java:473)\n\tat sun.net.www.http.HttpClient.<init>(HttpClient.java:203)\n\tat sun.net.www.http.HttpClient.New(HttpClient.java:290)\n\tat sun.net.www.http.HttpClient.New(HttpClient.java:306)\n\tat sun.net.www.protocol.http.HttpURLConnection.getNewHttpClient(HttpURLConnection.java:995)\n\tat sun.net.www.protocol.http.HttpURLConnection.plainConnect(HttpURLConnection.java:931)\n\tat sun.net.www.protocol.http.HttpURLConnection.connect(HttpURLConnection.java:849)\n\tat sun.net.www.protocol.http.HttpURLConnection.getInputStream(HttpURLConnection.java:1299)\n\tat java.net.URL.openStream(URL.java:1037)\n\tat com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser.createReader(RuntimeWSDLParser.java:827)nt... 69 more\n";
        ErrorMessage errorMessage = new ErrorMessage(messageString, stackTraceString, new WebServiceException(messageString));

        showErrorDialog(errorMessage, null, resolveInternalResourceBundle(Locale.getDefault()));
    }
*/


    public static void showErrorDialog(ErrorMessage message, ResourceBundle resourceBundle, ResourceBundle internalResourceBundle) {
        String title = resolveMessage(ERROR_DIALOG_TITLE_KEY, resourceBundle, internalResourceBundle);
        String okChoice = resolveMessage(BUTTON_OK_KEY, resourceBundle, internalResourceBundle);
        String logItChoice = resolveMessage(BUTTON_DETAILS_KEY, resourceBundle, internalResourceBundle);

        ErrorPanel errorPanel = new ErrorPanel(message, resourceBundle, internalResourceBundle);
        JOptionPane optionPanel = new JOptionPane(errorPanel, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[] {logItChoice, okChoice}, logItChoice);
        JDialog dialog = optionPanel.createDialog(null, title);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setResizable(true);
        dialog.pack();
        dialog.setLocationRelativeTo(findActiveOrVisibleFrame());
        dialog.setVisible(true);
        dialog.dispose();
        if (optionPanel.getValue() == logItChoice) {
            logger.log(Level.SEVERE, "", message.getThrowable());
        }
    }

    private static Frame findActiveOrVisibleFrame() {
        Frame[] frames = JFrame.getFrames();
        for (Frame frame : frames) {
            if (frame.isActive()) {
                return frame;
            }
        }
        for (Frame frame : frames) {
            if (frame.isVisible()) {
                return frame;
            }
        }
        return null;
    }

    //-------------------------------------------------------------------------

    private JTextArea txtErrorMessage;
    private JSeparator separator;
    private JCheckBox chkShowStackTrace;
    private JScrollPane scrollStackTrace;
    private JTextArea txtStackTrace;


    public ErrorPanel(ErrorMessage message, ResourceBundle resourceBundle, ResourceBundle internalResourceBundle) {
        setLayout(new BorderLayout());

        JPanel messagePanel = new JPanel();
        messagePanel.setBorder(new EmptyBorder(0, 0, 8, 0));
        messagePanel.setLayout(new BorderLayout());
        txtErrorMessage = new JTextArea();
        txtErrorMessage.setForeground(UIManager.getColor("Label.foreground"));
        txtErrorMessage.setBackground(UIManager.getColor("Label.background"));
        txtErrorMessage.setFont(UIManager.getFont("Label.font"));
        txtErrorMessage.setEditable(false);
        txtErrorMessage.setWrapStyleWord(true);
        txtErrorMessage.setText(message.getMessage());
        txtErrorMessage.setBorder(new EmptyBorder(8, 8, 16, 8));
        messagePanel.add(txtErrorMessage, BorderLayout.CENTER);

        separator = new JSeparator();
        messagePanel.add(separator, BorderLayout.SOUTH);

        add(messagePanel, BorderLayout.NORTH);

        if (message.getStackTrace() != null) {
            JPanel stackTracePanel = new JPanel();
            stackTracePanel.setBorder(new EmptyBorder(0, 0, 16, 0));
            stackTracePanel.setLayout(new BorderLayout());

            chkShowStackTrace = new JCheckBox(resolveMessage(SHOW_STACK_TRACE_CHECKBOX_KEY, resourceBundle, internalResourceBundle));
            String mnemonic = resolveMessage(SHOW_STACK_TRACE_CHECKBOX_MNEMONIC, resourceBundle, internalResourceBundle);
            if (mnemonic.length() != 1) throw new IllegalStateException("Mnemonic must be 1 letter");
            chkShowStackTrace.setMnemonic(mnemonic.charAt(0));
            chkShowStackTrace.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    toggleStackTrace();
                }
            });
            stackTracePanel.add(chkShowStackTrace, BorderLayout.NORTH);

            scrollStackTrace = new JScrollPane();
            scrollStackTrace.setVisible(false);

            txtStackTrace = new JTextArea();
            txtStackTrace.setEditable(false);
            txtStackTrace.setFont(new Font("Monospaced", txtStackTrace.getFont().getStyle(), txtStackTrace.getFont().getSize() + 3));
            txtStackTrace.setText(message.getStackTrace());

            scrollStackTrace.setViewportView(txtStackTrace);
            stackTracePanel.add(scrollStackTrace, BorderLayout.CENTER);

            add(stackTracePanel, BorderLayout.CENTER);
        }

        figureOutPreferredSize();
    }


    private void toggleStackTrace() {
        Point center = getCenterPoint();

        boolean show = chkShowStackTrace.isSelected();
        scrollStackTrace.setVisible(show);
        if (show) {
            scrollStackTrace.grabFocus();
        }

        figureOutPreferredSize();
        centerWindow(center);
    }

    private Point getCenterPoint() {
        Window parentWindow = getParentWindow();
        Point location = parentWindow.getLocation();
        Dimension size = parentWindow.getSize();
        return new Point(location.x + size.width / 2, location.y + size.height / 2);
    }

    private void figureOutPreferredSize() {
        Dimension maxSize = getMaxSize();

        txtErrorMessage.setLineWrap(false);
        Dimension preferredSize = txtErrorMessage.getPreferredSize();
        if (preferredSize.width > maxSize.width) {
            preferredSize.width = maxSize.width;
            txtErrorMessage.setLineWrap(true);
            txtErrorMessage.setSize(preferredSize);
            // And now, the txtErrorMessage.preferredSize is correct
        }

        if (scrollStackTrace != null) {
            Dimension scrollPreferredSize = scrollStackTrace.getPreferredSize();
            if (scrollPreferredSize.width > maxSize.width) {
                scrollPreferredSize.width = maxSize.width;
            }
            int scrollMaxHeight = maxSize.height - txtErrorMessage.getPreferredSize().height;
            if (scrollPreferredSize.height > scrollMaxHeight) {
                scrollPreferredSize.height = scrollMaxHeight;
            }
            scrollStackTrace.setPreferredSize(scrollPreferredSize);
        }
    }


    private Dimension getMaxSize() {
        Dimension screenSize = getToolkit().getScreenSize();
        return new Dimension((int) (screenSize.width * 0.6), (int) (screenSize.height * 0.6));
    }


    private void centerWindow(Point center) {
        Window parentWindow = getParentWindow();
        if (parentWindow != null) {
            parentWindow.pack();
            centerOnCenterPoint(parentWindow, center);
        }
    }

    private void centerOnCenterPoint(Window windowToCenter, Point center) {
        Dimension size = windowToCenter.getSize();
        Point location = new Point(center.x - size.width / 2, center.y - size.height / 2);
        if (location.x < 0) {
            location.x = 0;
        }
        if (location.y < 0) {
            location.y = 0;
        }
        windowToCenter.setLocation(location);
    }

    private Window getParentWindow() {
        JRootPane rootPane = getRootPane();
        if (rootPane == null) {
            return null;
        }
        return (Window) rootPane.getParent();
    }

    //-------------------------------------------------------------------------


    public static ResourceBundle resolveInternalResourceBundle(Locale locale) {
        return ResourceBundle.getBundle(
                ErrorPanel.class.getPackage().getName() + "/messages",
                locale,
                ErrorPanel.class.getClassLoader());
    }


    public static String resolveMessage(String key, ResourceBundle resourceBundle, ResourceBundle internalResourceBundle) {
        String text = null;
        try {
            if (resourceBundle != null) {
                text = resourceBundle.getString(key);
                if (text != null) return text;
            }
        } catch (MissingResourceException ex) {
            // Just silently ignore and use the built-in internalResourceBundle
        }

        try {
            text = internalResourceBundle.getString(key);
        } catch (MissingResourceException ex) {
            logger.log(Level.SEVERE, "Missing text in ResourceBundles for key '" + key + "'", ex);
            text = "---";
        } catch (NullPointerException ex) {
            logger.log(Level.SEVERE, "Missing internalResourceBundle", ex);
            text = "---";
        }
        return text;
    }


    public static class ErrorMessage {

        private final String message;
        private final String stackTrace;
        private Throwable throwable;


        public ErrorMessage(String message, String stackTrace, Throwable throwable) {
            this.message = message;
            this.stackTrace = stackTrace;
            this.throwable = throwable;
        }


        public ErrorMessage(String message) {
            this(message, null, null);
        }


        private String getMessage() {
            return message;
        }


        private String getStackTrace() {
            return stackTrace;
        }


        public Throwable getThrowable() {
            return throwable;
        }
    }
}
