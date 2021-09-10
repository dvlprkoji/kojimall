package com.example.kojimall.service;

import com.example.kojimall.domain.*;
import com.example.kojimall.repository.FlavorRepository;
import com.example.kojimall.repository.ItemRepository;
import com.example.kojimall.repository.ItemStcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

import static com.example.kojimall.domain.CodeVal.*;
import static com.example.kojimall.domain.DiscountRate.*;
import static java.lang.Math.round;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final FlavorRepository flavorRepository;
    private final ItemRepository itemRepository;
    private final ItemStcRepository itemStcRepository;

    public List<Tag> getTags(String word) {
        if (word.equals("")) {
            return Collections.emptyList();
        }
        List<Tag> tags = itemRepository.getTags(word);
        if (tags.isEmpty()) {
            log.info("no such tags");
        }
        return tags;
    }

    public List<Tag> getTags(List<String> words) {
        List<Tag> tagList = new ArrayList<>();
        for (String word : words) {
            tagList.add(itemRepository.getTag(word));
        }
        return tagList;
    }

    public void saveItem(Item item) {

    }

    public List<ItemTag> saveItemTagList(Item item, List<Tag> tagList) {
        List<ItemTag> itemTagList = new ArrayList<>();
        for (Tag tag : tagList) {
            ItemTag itemTag = new ItemTag();
            itemTag.setItem(item);
            itemTag.setTag(tag);
            itemRepository.saveItemTag(itemTag);
            itemTagList.add(itemTag);
        }
        return itemTagList;
    }


    public List<Item> getItemList(Integer startList, Integer endList) {
        return itemRepository.getItemList(startList, endList);
    }

    public Long getItemCnt(String category) {
        if (category == null || category.equals("all")) {
            return itemRepository.getItemCnt();
        } else {
            return itemRepository.getItemCnt(category);
        }
    }

    public Item getItem(Long id) {
        return itemRepository.getItem(id);
    }

    public ItemDtl getItemDtl(Item item, List<Image> imgList, List<Tag> tagList) {
        ItemDtl itemDtl = new ItemDtl();
        itemDtl.setItemId(item.getItemId());
        itemDtl.setItemNm(item.getItemNm());
        itemDtl.setItemDsc(item.getItemDsc());
        itemDtl.setCategory(item.getCategory());
        itemDtl.setItemRat(item.getItemRat());
        List<String> imgPathList = new ArrayList<>();
        for (Image image : imgList) {
            imgPathList.add(image.getPath());
        }
        itemDtl.setImgPathList(imgPathList);
        itemDtl.setTagList(tagList);
        return itemDtl;
    }

    public void setDscPrc(ItemDtl itemDtl, Member member) {
        Long dscPrc = itemDtl.getItemPrc();
        Double dscRate = NORMAL;
        if (member == null) {
            itemDtl.setItemDscPrc(itemDtl.getItemPrc());
            itemDtl.setItemDscAmt(round(0.0));
            return;
        }
        if (member.getSubYn().equals("Y")){
            dscRate += SUBSCRIBE;
        }
        if (member.getMemberRole().equals("VIP")) {
            dscRate += VIP;
        }

        dscPrc = round((double) dscPrc * (1-dscRate));
        itemDtl.setItemDscPrc(dscPrc);
        itemDtl.setItemDscAmt(itemDtl.getItemPrc() - dscPrc);

    }

    public List<Flavor> getFlavorList() {
        return flavorRepository.getFlavorList();
    }

    public Flavor getFlavor(Long id) {
        return flavorRepository.getFlavor(id);
    }

    public void saveItemStc(Item item,
                            Flavor flavor,
                            Long itemStcAmt,
                            String itemStcUnit,
                            Long itemPrc) {
        ItemStc itemStc = new ItemStc();
        itemStc.setItem(item);
        itemStc.setItemStcAmt(itemStcAmt);
        itemStc.setItemStcUnit(itemStcUnit);
        itemStc.setItemPrc(itemPrc);
        itemStc.setFlavor(flavor);
        itemStcRepository.saveItemStc(itemStc);
    }

    public List<Flavor> getFlavors(List<String> flavorIdList) {
        ArrayList<Flavor> flavorList = new ArrayList<>();
        for (String id : flavorIdList) {
            Flavor flavor = flavorRepository.getFlavor(Long.parseLong(id));
            flavorList.add(flavor);
        }
        return flavorList;
    }
}
