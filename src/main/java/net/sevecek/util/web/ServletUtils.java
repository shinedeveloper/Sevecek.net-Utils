package net.sevecek.util.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtils {

    public static void exposeModelToServletMachinery(Map<String, Object> model, HttpServletRequest request)
            throws ServletException, IOException {
        for (String modelObjectKey : model.keySet()) {
            Object modelObject = model.get(modelObjectKey);
            request.setAttribute(modelObjectKey, modelObject);
        }
    }


    public static RequestDispatcher prepareViewRenderer(ServletContext servletContext, String viewName) {
        return servletContext.getRequestDispatcher("/WEB-INF/view/" + viewName);
    }

    //--------------------------------------------------------------------
    // Pagination

    public static final int DEFAULT_PAGINATION_STEP = 10;
    public static final String PAGE_REQUEST_PARAMETER = "page";


    public static int resolvePaginationFirstItem(HttpServletRequest request) {
        int page = resolvePage(request) - 1;
        int firstItem = page * DEFAULT_PAGINATION_STEP;
        return firstItem;
    }


    public static int resolvePaginationCount(HttpServletRequest request) {
        return DEFAULT_PAGINATION_STEP;
    }


    public static int resolvePage(HttpServletRequest request) {
        String pageString = request.getParameter(PAGE_REQUEST_PARAMETER);
        if (pageString == null) {
            return 1;
        }
        return Integer.parseInt(pageString);
    }


    public static void exposePaginationToModel(HttpServletRequest request, Map<String, Object> model, List<?> entities) {
        int firstItem = resolvePaginationFirstItem(request);
        boolean hasPreviousPage = firstItem > 0;

        int count = resolvePaginationCount(request);
        boolean hasNextPage = entities.size() > count;
        if (hasNextPage) {
            // This is a trick to recognize that records are available
            // for the next page.
            // We select 1 more record and if successful,
            // the next page is available.
            // But we need to make a correction here and remove the extra record
            entities.remove(entities.size() - 1);
        }
        model.put("hasPreviousPage", hasPreviousPage);
        model.put("hasNextPage", hasNextPage);
        model.put("pageNumber", resolvePage(request));
    }


    public static void sendInputStream(HttpServletResponse response, InputStream stream) throws IOException {
        OutputStream output = response.getOutputStream();
        try {
            byte[] buffer = new byte[1024];
            while (true) {
                int actuallyRead = stream.read(buffer);
                if (actuallyRead == -1) break;
                output.write(buffer, 0, actuallyRead);
            }
        } finally {
            output.close();
        }

    }
}
