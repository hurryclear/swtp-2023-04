//index.js
import {createStore} from 'vuex';
import UniversityModule from './modules/university';
import ModuleModule from './modules/module';
import AuthenticationModule from './modules/authentication'
import FormModule from './modules/form'
import ApplicationFormModule from "@/store/modules/applicationForm";

export default createStore({
    modules: {
        university: UniversityModule,
        module: ModuleModule,
        authentication: AuthenticationModule,
        form: FormModule,
        applicationForm: ApplicationFormModule,
    },
});
