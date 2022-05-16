package jpabook.jpashop;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {

  private final InitService initService;

  @PostConstruct
  public void init() {
    initService.dbInit1();
    initService.dbInit2();
  }


  @Component
  @Transactional
  @RequiredArgsConstructor
  static class InitService {

    private final EntityManager em;
    public void dbInit1() {
      Member member = new Member();
      member.setName("userA");
      member.setAddress(new Address("서울", "1", "1111"));
      em.persist(member);

      Book book = getBook("JPA1", 10000, 100);
      em.persist(book);

      Book book2 = getBook("JPA2", 20000, 100);
      em.persist(book2);

      OrderItem orderItem1 = OrderItem.createOrderItem(book, 10000, 1);
      OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

      Delivery delivery = new Delivery();
      delivery.setAddress(member.getAddress());
      Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
      em.persist(order);
    }

    public void dbInit2() {
      Member member = new Member();
      member.setName("userB");
      member.setAddress(new Address("부산", "1", "1111"));
      em.persist(member);

      Book book = getBook("SPRING1", 20000, 100);
      em.persist(book);

      Book book2 = getBook("SPRING2", 40000, 100);
      em.persist(book2);

      OrderItem orderItem1 = OrderItem.createOrderItem(book, 20000, 1);
      OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 2);

      Delivery delivery = new Delivery();
      delivery.setAddress(member.getAddress());
      Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
      em.persist(order);
    }
  }

  private static Book getBook(String name, int price, int stockQuantity) {
    Book book = new Book();
    book.setName(name);
    book.setPrice(price);
    book.setStockQuantity(stockQuantity);
    return book;
  }


}
