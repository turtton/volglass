import { AppProps } from "next/app";
import "reactflow/dist/style.css";
import "../styles/global.css";
import "../styles/prism-dark.css";
import "../styles/prism.css";
import "../styles/style.css";

export default function App({ Component, pageProps }: AppProps): JSX.Element {
	return <Component {...pageProps} />;
}
