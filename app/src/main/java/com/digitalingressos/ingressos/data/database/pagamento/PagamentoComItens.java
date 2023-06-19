package com.digitalingressos.ingressos.data.database.pagamento;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.digitalingressos.ingressos.data.database.pagamentoItem.PagamentoItem;

import java.util.List;

public class PagamentoComItens {
    @Embedded
    public Pagamento pagamento;

    @Relation(parentColumn = "id", entityColumn = "pagamento_id", entity = PagamentoItem.class)
    public List<PagamentoItem> pagamentoItemList;
}
