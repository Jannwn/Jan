package entity;
//import org.apache.hc.client5.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OneNoteAPIClient {
    // OneNote API的终结点和应用程序凭据
    private static final String API_ENDPOINT = "https://graph.microsoft.com/v1.0/me/onenote/pages";
    private static final String CLIENT_ID = "YOUR_CLIENT_ID";
    private static final String CLIENT_SECRET = "YOUR_CLIENT_SECRET";
    private static final String REDIRECT_URI = "YOUR_REDIRECT_URI";
    private static final String SCOPE = "https://graph.microsoft.com/.default";

    // 获取访问令牌的函数
    private static String getAccessToken() throws IOException {
        String tokenUrl = "https://login.microsoftonline.com/YOUR_TENANT_ID/oauth2/v2.0/token";
        HttpPost httpPost = new HttpPost(tokenUrl);

        // 构建请求体
        String requestBody = "grant_type=client_credentials" +
                "&client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&redirect_uri=" + REDIRECT_URI +
                "&scope=" + SCOPE;

        httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_FORM_URLENCODED));

        // 发送请求
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpResponse response = httpClient.execute(httpPost);

            // 解析响应
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            }
        }
    }

    // 使用访问令牌创建笔记
    private static void createNote() throws IOException {
        String accessToken = getAccessToken();

        // 在这里构建你的笔记的数据
        String noteData = "<html><head><title>Sample Page</title></head><body><p>Hello, OneNote!</p></body></html>";

        // 发送请求创建笔记
        HttpPost httpPost = new HttpPost(API_ENDPOINT);
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "text/html");
        httpPost.setEntity(new StringEntity(noteData, ContentType.APPLICATION_XML));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpResponse response = httpClient.execute(httpPost);

            // 处理响应
            if (response.getStatusLine().getStatusCode() == 201) {
                System.out.println("笔记创建成功！");
            } else {
                System.err.println("笔记创建失败：" + response.getStatusLine().getReasonPhrase());
            }
        }
    }

    public static void main(String[] args) {
        try {
            createNote();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}