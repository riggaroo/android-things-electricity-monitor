package za.co.riggaroo.electricitymonitor;


public interface OverviewContract {

    interface View {
        void updateViewModel(ElectricityViewModel electricityViewModel);
    }

    interface Presenter {
        void attach(View view);

        void detach();

        void loadPowerInformation();
    }
}
