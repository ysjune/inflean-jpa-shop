package jpabook.jpashop.service;

import java.util.List;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

  private final ItemRepository itemRepository;

  public void saveItem(Item item) {
    itemRepository.save(item);
  }

  public void updateItem(Long itemId, Book bookParam) {
    Item findItem = itemRepository.findOne(itemId);
    findItem.setPrice(bookParam.getPrice());
    findItem.setName(bookParam.getName());
    findItem.setStockQuantity(bookParam.getStockQuantity());
  }

  public List<Item> findItems() {
    return itemRepository.findAll();
  }

  public Item findOne(Long itemId) {
    return itemRepository.findOne(itemId);
  }
}
