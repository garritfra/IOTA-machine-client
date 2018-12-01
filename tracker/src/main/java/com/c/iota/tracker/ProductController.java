package com.c.iota.tracker;

import com.google.gson.JsonObject;
import jota.model.Transaction;
import jota.utils.Converter;
import jota.utils.TrytesConverter;
import jota.IotaAPI;
import jota.dto.response.GetNodeInfoResponse;
import jota.dto.response.GetTransferResponse;
import jota.error.ArgumentException;
import jota.model.Bundle;
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
                        t-> new Product(toFragment(t), t.getTimestamp())
                ).collect(Collectors.toList());
//        String payload = response[0].getTransactions().get(0).getSignatureFragments();

        model.addAttribute("id", fragments);
        return "index";

    }
    
    @RequestMapping("/")
    public String index(Model model) throws ArgumentException {
        return "product";

    }

    private String toFragment(final Transaction t) {
        return TrytesConverter.trytesToAscii(t.getSignatureFragments().substring(0, t.getSignatureFragments().indexOf("9")));
    }
}
