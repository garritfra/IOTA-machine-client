package com.c.iota.tracker;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jota.model.Transaction;
import jota.utils.Converter;
import jota.utils.TrytesConverter;
import jota.IotaAPI;
import jota.dto.response.GetNodeInfoResponse;
import jota.dto.response.GetTransferResponse;
import jota.error.ArgumentException;
import jota.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @RequestMapping("product/{id}")
    public String showSteps(@PathVariable("id") String id, Model model) throws ArgumentException {
        IotaAPI api = new IotaAPI.Builder()
                .protocol("https")
                .host("testnet140.tangle.works")
                .port("443")
                .build();
//        GetNodeInfoResponse response = api.getNodeInfo();
        Bundle[] response = api.bundlesFromAddresses(new String[]{id},
                 true);

        List<Product> fragments = Stream.of(response)
                .flatMap(b -> b.getTransactions().stream()).map(
                        t-> parse(toFragment(t), t.getTimestamp())
                )
                .sorted((p1, p2) -> Math.toIntExact((p1.getTime() - p2.getTime())))
                .collect(Collectors.toList());


        model.addAttribute("products", fragments);
        model.addAttribute("id", fragments);

        return "product";
    }

    @RequestMapping("/")
    public String index(Model model) throws ArgumentException {
        return "index";

    }

    private Product parse(String json, long timestamp){
        Gson gson = new Gson();
        LOGGER.info("parsing: " + json);
        Product product = gson.fromJson(json, Product.class);
        product.setTime(timestamp);
        return product;
    }

    private String toFragment(final Transaction t) {
        return TrytesConverter.trytesToAscii(t.getSignatureFragments().substring(0, t.getSignatureFragments().indexOf("999999999")));
    }
}
