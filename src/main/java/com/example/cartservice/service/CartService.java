package com.example.cartservice.service;
import com.example.cartservice.pojo.Cart;
import com.example.cartservice.pojo.CreatePaymentRestModel;
import com.example.cartservice.pojo.OrderRestModel;
import com.example.cartservice.pojo.Product;
import com.example.cartservice.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.*;

@Service
public class CartService {
    @Autowired
    private CartRepository repository;

    public CartService(CartRepository repository){
        this.repository = repository;
    }

    private ResponseEntity<Map<String, Object>> getProductPartial(String product_id) {
        String apiUrl = "https://product-service-908649839259189283.rcf2.deploys.app/api/products/id/" + product_id;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(apiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Cart createCart(Cart cart) {
        for (Product newProduct : cart.getProduct()) {
            boolean found = false;
            if (cart.getProduct() != null) {
                for (Product existingProduct : cart.getProduct()) {
                    if (existingProduct.get_id().equals(newProduct.get_id())) {
                        ResponseEntity<Map<String, Object>> productResponse = getProductPartial(existingProduct.get_id());
                        if (productResponse.getStatusCode().is2xxSuccessful()) {
                            Map<String, Object> productData = productResponse.getBody();
                            existingProduct.setRating((double) productData.get("rating"));
                            existingProduct.setPrice((double) productData.get("price"));
                            existingProduct.setName((String) productData.get("name"));
                            existingProduct.setImg_path((String) productData.get("img_path"));
                            existingProduct.setDescription((String) productData.get("description"));
                            existingProduct.setCategory((String) productData.get("category"));
                            existingProduct.setShop_id((String) productData.get("shop_id"));
                            existingProduct.setEdit_at((String) productData.get("edit_at"));
                            existingProduct.setCreate_at((String) productData.get("create_at"));
                        } else {
                            throw new IllegalArgumentException("ไม่พบ Product");
                        }
                        existingProduct.setAmount(existingProduct.getAmount() + 1);
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                if (cart.getProduct() == null) {
                    cart.setProduct(new ArrayList<>());
                }
                cart.getProduct().add(newProduct);
            }
        }

        return repository.save(cart);
    }

    public Cart updateCart(String id, Cart cart) {
        Cart existingCart = repository.findByCustomerId(id);

        if (existingCart != null) {
            List<Product> existingProducts = existingCart.getProduct();

            if (existingProducts == null) {
                existingProducts = new ArrayList<>();
                existingCart.setProduct(existingProducts);
            }

            for (Product newProduct : cart.getProduct()) {
                boolean found = false;

                for (Product existingProduct : existingProducts) {
                    if (existingProduct.get_id().equals(newProduct.get_id())) {
                        ResponseEntity<Map<String, Object>> productResponse = getProductPartial(existingProduct.get_id());
                        if (productResponse.getStatusCode().is2xxSuccessful()) {
                            Map<String, Object> productData = productResponse.getBody();
                            existingProduct.setRating((double) productData.get("rating"));
                            existingProduct.setPrice((double) productData.get("price"));
                            existingProduct.setName((String) productData.get("name"));
                            existingProduct.setImg_path((String) productData.get("img_path"));
                            existingProduct.setDescription((String) productData.get("description"));
                            existingProduct.setCategory((String) productData.get("category"));
                            existingProduct.setShop_id((String) productData.get("shop_id"));
                            existingProduct.setEdit_at((String) productData.get("edit_at"));
                            existingProduct.setCreate_at((String) productData.get("create_at"));
                        } else {
                            throw new IllegalArgumentException("ไม่พบ Product");
                        }
                        existingProduct.setAmount(existingProduct.getAmount() + 1);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    ResponseEntity<Map<String, Object>> productResponse = getProductPartial(newProduct.get_id());
                    if (productResponse.getStatusCode().is2xxSuccessful()) {
                        Map<String, Object> productData = productResponse.getBody();
                        newProduct.setRating((double) productData.get("rating"));
                        newProduct.setPrice((double) productData.get("price"));
                        newProduct.setName((String) productData.get("name"));
                        newProduct.setImg_path((String) productData.get("img_path"));
                        newProduct.setDescription((String) productData.get("description"));
                        newProduct.setCategory((String) productData.get("category"));
                        newProduct.setShop_id((String) productData.get("shop_id"));
                        newProduct.setEdit_at((String) productData.get("edit_at"));
                        newProduct.setCreate_at((String) productData.get("create_at"));
                    } else {
                        throw new IllegalArgumentException("ไม่พบ Product");
                    }
                    newProduct.setAmount(1);
                    existingProducts.add(newProduct);
                }
            }

            return repository.save(existingCart);
        }

        return null;
    }

    public Cart delCartProduct(String id, Cart cart) {
        Cart existingCart = repository.findByCustomerId(id);

        if (existingCart != null) {
            List<Product> existingProducts = existingCart.getProduct();
            List<Product> newProducts = cart.getProduct();

            if (existingProducts != null) {
                for (Product newProduct : newProducts) {
                    boolean found = false;

                    for (Product existingProduct : existingProducts) {
                        if (existingProduct.get_id().equals(newProduct.get_id())) {
                            found = true;

                            if (existingProduct.getAmount() > 0) {
                                existingProduct.setAmount(existingProduct.getAmount() - 1);

                                if (existingProduct.getAmount() == 0) {
                                    existingProducts.remove(existingProduct);
                                }
                            } else {
                                existingProducts.remove(existingProduct);
                            }

                            break;
                        }
                    }

                    if (!found) {
                        throw new IllegalArgumentException("ไม่พบ Product");
                    }
                }
            }

            existingCart.setProduct(existingProducts);
            return repository.save(existingCart);
        } else {
            return null;
        }
    }

    public Cart clearProduct(String id) {
        Cart existingCart = repository.findByCustomerId(id);
        if (existingCart != null) {
            existingCart.setProduct(new ArrayList<>());
            return repository.save(existingCart);
        } else {
            return null;
        }
    }


    public boolean deleteCart(String id){
        if (!repository.findById(id).isEmpty()) {
            repository.deleteById(id);
            return true;
        }
        return false;

    }

    public Optional<Cart> getCartById(String id){
        return repository.findById(id);
    }

    public List<Cart> getCart(){
        return repository.findAll();
    }

    private ResponseEntity<Map<String, Object>> getSumPrice(double price) {
        String apiUrl = "https://tax-service2-908649839259189283.rcf2.deploys.app/calculate_tax";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("price", price);
        requestBody.put("currency", "THB");
        requestBody.put("payment_date", "2023-09-30T14:30:00Z");
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(apiUrl, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, Object>>() {});
    }


    public double calculateTotalPrice(String user_id) {
        Cart cart = repository.findByCustomerId(user_id);
        double totalPrice = 0;

        for (Product product : cart.getProduct()) {
            double price = product.getPrice();
            int amount = product.getAmount();
            totalPrice += price * amount;
        }
        ResponseEntity<Map<String, Object>> priceResponse = getSumPrice(totalPrice);
        Map<String, Object> priceSum = priceResponse.getBody();
        if (priceSum != null) {
            Map<String, Object> data = (Map<String, Object>) priceSum.get("data");
            double totalPriceSum = (double) data.get("total_price");
            return totalPriceSum;
        } else {
            throw new IllegalArgumentException("คำนวณผิดพลาด");
        }
    }

    public double notVat(String user_id) {
        Cart cart = repository.findByCustomerId(user_id);
        double totalPrice = 0;
        for (Product product : cart.getProduct()) {
            double price = product.getPrice();
            int amount = product.getAmount();
            totalPrice += price * amount;
        }
        return  totalPrice;
    }

    private ResponseEntity<Map<String, Object>> createOrder(Cart cart, String token) {
        String apiUrl = "https://order-908649839259189283.rcf2.deploys.app/api/orders";

        double total_price = calculateTotalPrice(cart.getCustomer_id());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("total_cost", total_price);
        List<Map<String, Object>> productDetails = new ArrayList<>();
        for (Product product : cart.getProduct()) {
            Map<String, Object> productMap = new HashMap<>();
            productMap.put("_id", product.get_id());
            productMap.put("name", product.getName());
            productMap.put("description", product.getDescription());
            productMap.put("img_path", product.getImg_path());
            productMap.put("price", product.getPrice());
            productMap.put("category", product.getCategory());
            productMap.put("shop_id", product.getShop_id());
            productMap.put("rating", product.getRating());
            productDetails.add(productMap);
        }

        requestBody.put("products", productDetails);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(apiUrl, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    private ResponseEntity<Map<String, Object>> CheckActive(String user_id) {
        String apiUrl = "https://02dc-2405-9800-b861-1f19-b5c8-8634-d7e2-c8a3.ngrok-free.app/api/wallets/activate";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_id", user_id);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(apiUrl, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    private ResponseEntity<Map<String, Object>> CheckWallet(String user_id, String token) {
        String apiUrl = "https://02dc-2405-9800-b861-1f19-b5c8-8634-d7e2-c8a3.ngrok-free.app/api/wallets";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(apiUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    private ResponseEntity<Map<String, Object>> getWalletFromShop(String shop_id) {
        String apiUrl = "https://shop-908649839259189283.rcf2.deploys.app/api/shops/"+ shop_id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(apiUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    private ResponseEntity<Map<String, Object>> goToPayment(String token, CreatePaymentRestModel data) {
        String apiUrl = "https://02dc-2405-9800-b861-1f19-b5c8-8634-d7e2-c8a3.ngrok-free.app/api/wallets/payment";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreatePaymentRestModel> entity = new HttpEntity<>(data, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(apiUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public String createOrder(String user_id, String token) {
        Cart cart = repository.findByCustomerId(user_id);
        double total_price = calculateTotalPrice(user_id);
        double notVat = notVat(user_id);
        Map<String, Double> shopTotalCostMap = new HashMap<>();
        Map<String, Double> shopWalletMap = new HashMap<>();

        try {
            ResponseEntity<Map<String, Object>> response = CheckActive(user_id);
            Map<String, Object> responseBody = response.getBody();
            throw new IllegalArgumentException("Wallet ท่านไม่พอ");

        } catch (Exception e) {
            ResponseEntity<Map<String, Object>> responseEntity = CheckWallet(user_id, token);
            Map<String, Object> responseBody = responseEntity.getBody();
            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
            if (data != null) {
                Map<String, Object> wallet = (Map<String, Object>) data.get("wallet");
                if (wallet != null) {
                    Map<String, Object> info = (Map<String, Object>) wallet.get("info");
                    if (info != null) {
                        Object balanceObject = info.get("balance");
                        if (balanceObject instanceof Integer) {
                            Integer balanceInteger = (Integer) balanceObject;
                            double balance = balanceInteger.doubleValue();

                            if (total_price <= balance) {
                                for (Product product : cart.getProduct()) {
                                    String shopId = product.getShop_id();
                                    double productTotalCost = product.getPrice() * product.getAmount();

                                    if (shopTotalCostMap.containsKey(shopId)) {
                                        double existingTotalCost = shopTotalCostMap.get(shopId);
                                        ResponseEntity<Map<String, Object>> responseEntity2 = getWalletFromShop(shopId);
                                        Map<String, Object> responseBody2 = responseEntity2.getBody();
                                        shopWalletMap.put((String) responseBody2.get("account"), existingTotalCost + productTotalCost);
                                        shopTotalCostMap.put(shopId, existingTotalCost + productTotalCost);
                                    } else {
                                        ResponseEntity<Map<String, Object>> responseEntity2 = getWalletFromShop(shopId);
                                        Map<String, Object> responseBody2 = responseEntity2.getBody();
                                        shopWalletMap.put((String) responseBody2.get("account"), productTotalCost);
                                        shopTotalCostMap.put(shopId, productTotalCost);
                                    }

                                }

                                List <OrderRestModel> orders= new ArrayList<>();
                                CreatePaymentRestModel restOrderPaymentMhukrob = CreatePaymentRestModel.builder().tax(BigDecimal.valueOf(notVat*0.07)).amount(BigDecimal.valueOf(notVat)).build();
                                for (Map.Entry<String, Double> entry : shopWalletMap.entrySet()) {
                                    OrderRestModel order = new OrderRestModel(entry.getKey(),BigDecimal.valueOf(entry.getValue()));
                                    orders.add(order);
                                    System.out.println("Wallet ID: " + entry.getKey() + ", Total Cost: " + entry.getValue());
                                }
                                restOrderPaymentMhukrob.setOrders(orders);
                                System.out.println("restPayment"+restOrderPaymentMhukrob);
                                ResponseEntity<Map<String, Object>> theEnd = goToPayment(token, restOrderPaymentMhukrob);


                                ResponseEntity<Map<String, Object>> priceResponse = createOrder(cart, token);
                                Map<String, Object> priceSum = priceResponse.getBody();
                                if (priceSum != null) {
                                    return (String) priceSum.get("_id");
                                } else {
                                    throw new IllegalArgumentException("สร้าง order ผิดพลาด");
                                }
                            } else {
                                throw new IllegalArgumentException("เงินไม่พอ");
                            }
                        } else if (balanceObject instanceof Double) {
                            double balance = (double) balanceObject;

                            if (total_price <= balance) {
                                for (Product product : cart.getProduct()) {
                                    String shopId = product.getShop_id();
                                    double productTotalCost = product.getPrice() * product.getAmount();

                                    if (shopTotalCostMap.containsKey(shopId)) {
                                        double existingTotalCost = shopTotalCostMap.get(shopId);
                                        ResponseEntity<Map<String, Object>> responseEntity2 = getWalletFromShop(shopId);
                                        Map<String, Object> responseBody2 = responseEntity2.getBody();
                                        shopWalletMap.put((String) responseBody2.get("account"), existingTotalCost + productTotalCost);
                                        shopTotalCostMap.put(shopId, existingTotalCost + productTotalCost);
                                    } else {
                                        ResponseEntity<Map<String, Object>> responseEntity2 = getWalletFromShop(shopId);
                                        Map<String, Object> responseBody2 = responseEntity2.getBody();
                                        shopWalletMap.put((String) responseBody2.get("account"), productTotalCost);
                                        shopTotalCostMap.put(shopId, productTotalCost);
                                    }

                                }

                                List <OrderRestModel> orders= new ArrayList<>();
                                CreatePaymentRestModel restOrderPaymentMhukrob = CreatePaymentRestModel.builder().tax(BigDecimal.valueOf(notVat*0.07)).amount(BigDecimal.valueOf(notVat)).build();
                                for (Map.Entry<String, Double> entry : shopWalletMap.entrySet()) {
                                    OrderRestModel order = new OrderRestModel(entry.getKey(),BigDecimal.valueOf(entry.getValue()));
                                    orders.add(order);
                                    System.out.println("Wallet ID: " + entry.getKey() + ", Total Cost: " + entry.getValue());
                                }
                                restOrderPaymentMhukrob.setOrders(orders);
                                System.out.println("restPayment"+restOrderPaymentMhukrob);
                                ResponseEntity<Map<String, Object>> theEnd = goToPayment(token, restOrderPaymentMhukrob);
                                ResponseEntity<Map<String, Object>> priceResponse = createOrder(cart, token);
                                Map<String, Object> priceSum = priceResponse.getBody();

                                if (priceSum != null) {
                                    return (String) priceSum.get("_id");
                                } else {
                                    throw new IllegalArgumentException("สร้าง order ผิดพลาด");
                                }
                            } else {
                                throw new IllegalArgumentException("เงินไม่พอ");
                            }
                        } else {
                            System.out.println("ไม่สามารถแปลง balance เป็น double ได้");
                        }

                    }
                }
            }


        }

        return "success";
    }
    public Cart getCartByCustomerId(String customer_id){
        Cart cart = repository.findByCustomerId(customer_id);
        if (cart == null) {
            throw new IllegalArgumentException("ไม่พบ cart");
        }
        return cart;
    }

}
