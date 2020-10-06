package it.niedermann.owncloud.notes.manageaccounts;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.niedermann.owncloud.notes.R;
import it.niedermann.owncloud.notes.persistence.entity.LocalAccountEntity;

public class ManageAccountAdapter extends RecyclerView.Adapter<ManageAccountViewHolder> {

    @Nullable
    private LocalAccountEntity currentLocalAccount = null;
    @NonNull
    private final List<LocalAccountEntity> localAccounts = new ArrayList<>();
    @NonNull
    private final Consumer<LocalAccountEntity> onAccountClick;
    @Nullable
    private final Consumer<LocalAccountEntity> onAccountDelete;

    public ManageAccountAdapter(@NonNull Consumer<LocalAccountEntity> onAccountClick, @Nullable Consumer<LocalAccountEntity> onAccountDelete) {
        this.onAccountClick = onAccountClick;
        this.onAccountDelete = onAccountDelete;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return localAccounts.get(position).getId();
    }

    @NonNull
    @Override
    public ManageAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ManageAccountViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_choose, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ManageAccountViewHolder holder, int position) {
        final LocalAccountEntity localAccount = localAccounts.get(position);
        holder.bind(localAccount, (localAccountClicked) -> {
            setCurrentLocalAccount(localAccountClicked);
            onAccountClick.accept(localAccountClicked);
        }, (localAccountToDelete -> {
            if (onAccountDelete != null) {
                for (int i = 0; i < localAccounts.size(); i++) {
                    if (localAccounts.get(i).getId() == localAccountToDelete.getId()) {
                        localAccounts.remove(i);
                        notifyItemRemoved(i);
                        break;
                    }
                }
                onAccountDelete.accept(localAccountToDelete);
            }
        }), currentLocalAccount != null && currentLocalAccount.getId() == localAccount.getId());
    }

    @Override
    public int getItemCount() {
        return localAccounts.size();
    }

    public void setLocalAccounts(@NonNull List<LocalAccountEntity> localAccounts) {
        this.localAccounts.clear();
        this.localAccounts.addAll(localAccounts);
        notifyDataSetChanged();
    }

    public void setCurrentLocalAccount(@Nullable LocalAccountEntity currentLocalAccount) {
        this.currentLocalAccount = currentLocalAccount;
        notifyDataSetChanged();
    }
}
