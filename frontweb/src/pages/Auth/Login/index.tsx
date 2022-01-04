import { Link, useHistory, useLocation } from "react-router-dom";
import ButtonIcon from "components/ButtonIcon";
import { useForm } from "react-hook-form";
import { requestBackendLogin} from "util/requests";
import { useState, useContext } from "react";
import { AuthContext } from "AuthContext";
import { saveAuthData } from "util/storage";
import { getTokenData } from "util/auth";

import "./styles.css";

type CredentialsDTO = {
  username: string;
  password: string;
};

type LocationState = {
  from: string;
}

const Login = () => {

  const location = useLocation<LocationState>();

  const { from } = location.state || { from: { pathname: '/admin' } };

  const {setAuthContextData } = useContext(AuthContext);

  const [hasError, setHasError] = useState(false);

  const { register, handleSubmit, formState: { errors } } = useForm<CredentialsDTO>();

  const history = useHistory();

  const onSubmit = (formData: CredentialsDTO) => {
    console.log(formData);
    requestBackendLogin(formData)
      .then((response) => {
        saveAuthData(response.data);
        setHasError(false);
        setAuthContextData({
          authenticated: true,
          tokenData: getTokenData(),
        })
        console.log("SUCESSO", response);
        history.replace(from);
      })
      .catch((error) => {
        setHasError(true);
        console.log("ERRO", error);
      });
  };

  return (
    <div className="base-card login-card">
      <h1>LOGIN</h1>
      {hasError && (
        <div className="alert alert-danger">Erro ao tentar efetuar o login</div>
      )}
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="mb-4">
          <input
            {...register("username", {
              required: "Campo obrigatório",
              pattern: {
                value: /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/,
                message: 'Email invalido'
              }
            })}
            type="text"
            className={`form-control base-input ${errors.username ? 'is-invalid': ''}`} 
            placeholder="Email"
            name="username"
          />
          <div className="invalid-feedback d-block">{errors.username?.message}</div>
        </div>
        <div className="mb-2">
          <input
            {...register("password", {
              required: 'Campo obrigatório'
            })}
            type="password"
            className={`form-control base-input ${errors.password ? 'is-invalid': ''}`}
            placeholder="Password"
            name="password"
          />
         
          <div className="invalid-feedback d-block">{errors.password?.message}</div>
        </div>
        <Link to="/admin/auth/recover" className="login-link-recover">
          Esqueci a senha
        </Link>
        <div className="login-submit">
          <ButtonIcon text="Fazer login" />
        </div>
        <div className="signup-container">
          <span className="not-registered">Não tem Cadastro?</span>
          <Link to="/admin/auth/signup" className="login-link-register">
            CADASTRAR
          </Link>
        </div>
      </form>
    </div>
  );
};

export default Login;
