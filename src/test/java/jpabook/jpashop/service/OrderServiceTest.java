package jpabook.jpashop.service;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import javax.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

  @Autowired
  private EntityManager em;

  @Autowired
  private OrderService orderService;

  @Autowired
  private OrderRepository orderRepository;

  @Test
  void 상품주문() {
    // given
    Member member = createMember();

    Book book = createBook("시골 JPA", 10000);

    int orderCount = 2;
    // when
    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

    // then
    Order getOrder = orderRepository.findOne(orderId);

    assertEquals("상품 주문시 상태는 Order", OrderStatus.ORDER, getOrder.getStatus());
    assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
    assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, getOrder.getTotalPrice());
    assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, book.getStockQuantity());
  }



  @Test
  void 상품주문_재고수량초과() {
    // given
    Member member = createMember();

    Book book = createBook("시골 JPA", 10000);

    int orderCount = 12;
    // when
    assertThrows(NotEnoughStockException.class, () ->
        orderService.order(member.getId(), book.getId(), orderCount));
  }

  @Test
  void 주문취소() {
    // given
    Member member = createMember();

    Book book = createBook("시골 JPA", 10000);
    int orderCount = 2;
    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
    // when
    orderService.cancelOrder(orderId);

    // then
    Order getOrder = orderRepository.findOne(orderId);

    assertEquals("주문 취소시 상태는 CANCEL 이다", OrderStatus.CANCEL, getOrder.getStatus());
    assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, book.getStockQuantity());
  }


  private Member createMember() {
    Member member = new Member();
    member.setName("회원1");
    member.setAddress(new Address("서울", "강가", "111111"));
    em.persist(member);
    return member;
  }

  private Book createBook(String name, int orderPrice) {
    Book book = new Book();
    book.setName(name);
    book.setPrice(orderPrice);
    book.setStockQuantity(10);
    em.persist(book);
    return book;
  }
}