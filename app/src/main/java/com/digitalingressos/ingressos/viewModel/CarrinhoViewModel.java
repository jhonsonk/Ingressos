package com.digitalingressos.ingressos.viewModel;

import androidx.lifecycle.ViewModel;

import com.digitalingressos.ingressos.application.IngressosApplication;
import com.digitalingressos.ingressos.data.IngressoPosse;
import com.digitalingressos.ingressos.data.database.checkout.Checkout;
import com.digitalingressos.ingressos.data.database.checkout.CheckoutItem;
import com.digitalingressos.ingressos.data.database.checkout.CheckoutItemPosse;
import com.digitalingressos.ingressos.data.database.checkout.CheckoutRequest;
import com.digitalingressos.ingressos.data.database.evento.Evento;
import com.digitalingressos.ingressos.data.database.ingresso.Ingresso;
import com.digitalingressos.ingressos.util.UtilMoney;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


public class CarrinhoViewModel extends ViewModel {
    private final List<IngressoPosse> mListIngressos = new ArrayList<>();

    private IngressoPosse pagadorPadrao;
    private Checkout mCheckout;

    public boolean isCheckBoxCheckedTodos() {
        return isCheckBoxCheckedTodos;
    }

    public void setCheckBoxCheckedTodos(boolean checkBoxCheckedTodos) {
        isCheckBoxCheckedTodos = checkBoxCheckedTodos;
    }

    boolean isCheckBoxCheckedTodos = false;

    public Evento mEvento = null;

    public IngressoPosse getPagadorPadrao() {
        return pagadorPadrao;
    }

    public void setPagadorPadrao(IngressoPosse pagadorPadrao) {
        this.pagadorPadrao = pagadorPadrao;
    }

    public Checkout getCheckout() {
        return mCheckout;
    }

    public void setCheckout(Checkout checkout) {

        List<CheckoutItem> checkoutItemList = checkout.getCheckoutItem().stream()
                .map(objeto -> new CheckoutItem(objeto.getUuidValor(), objeto.getQuantidade(), objeto.getPosses())).collect(Collectors.toList());


        for (IngressoPosse _ingressoPosse : mListIngressos) {
            Optional<CheckoutItem> checkoutItemOptional = checkoutItemList.stream().filter(a -> a.getUuidValor().equals(_ingressoPosse.geValorLoteIngressoUuid())).findFirst();
            if (checkoutItemOptional.isPresent()) {
                int quantidade = _ingressoPosse.mIngresso.getCombo();

                List<CheckoutItemPosse> itemList = checkoutItemOptional.get().getPosses().subList(0, quantidade);
                _ingressoPosse.mCheckoutItemPosse = new ArrayList<>();
                _ingressoPosse.mCheckoutItemPosse.addAll(itemList);
                checkoutItemOptional.get().getPosses().removeAll(itemList);
            }
        }

        this.mCheckout = checkout;
    }

    public boolean isTodosIngressosPreenchidos() {

        return mListIngressos.stream()
                .allMatch(ingressoPosse -> ingressoPosse.cpfPosse != null && !ingressoPosse.cpfPosse.isEmpty() &&
                        ingressoPosse.telefonePosse != null && !ingressoPosse.telefonePosse.isEmpty() &&
                        !ingressoPosse.cpfError && !ingressoPosse.telefoneError);
    }

    public void setEvento(Evento evento) {
        this.mEvento = evento;
    }

    public Evento getEvento() {
        return mEvento;
    }

    public int getSizeCarrinho() {
        return mListIngressos.size();
    }

    public void insertIngresso(Ingresso ingresso) {

        mListIngressos.add(new IngressoPosse(ingresso));
    }

    public void removeIngresso(Ingresso ingresso) {
        mListIngressos.remove(new IngressoPosse(ingresso));
    }

    //ok
    public String getPrecoTotalMonetary() {
        return UtilMoney.parseDoubleToMoney(getPrecoTotal(), IngressosApplication.getLocale());
    }


    public List<CheckoutRequest> getCheckoutRequest() {
        return mListIngressos.stream()
                .collect(Collectors.groupingBy(item -> item.valorLoteIngressoUuid, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new CheckoutRequest(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }


    public List<IngressoPosse> getSelectedItemList() {
        return mListIngressos;
    }


    public String getPrecoTotalString() {
        UtilMoney.parseDoubleToMoney(getPrecoTotal(), IngressosApplication.getLocale());
        DecimalFormat dFormat = new DecimalFormat("#.00");
        double vlor = getPrecoTotal();
        if (vlor == 0) {
            return "R$0,00";
        }
        return "R$" + dFormat.format(vlor);
    }


    public List<IngressoPosse> getIngressos(String valorUuid) {
        return mListIngressos.stream().filter(elemento -> elemento.geValorLoteIngressoUuid().equals(valorUuid)).collect(Collectors.toList());
    }

    public double getPrecoTotal() {
        return mListIngressos.stream().mapToDouble(x -> x.mIngresso.getValor()).sum();
    }

    public int getPrecoTotalInt() {

        return Integer.parseInt(getPrecoTotalString().replaceAll("R$", "").replaceAll("[^0-9]*", ""));

    }


    public IngressoPosse get(int position) {
        return mListIngressos.get(position);
    }


    public long getQuantidade(String valorUuid) {
        return getIngressos(valorUuid).size();
    }

}