import { styled } from "@mui/material/styles";
import { FormControlLabel, PaletteMode, Switch } from "@mui/material";
import { useMemo, useState } from "react";
import { createTheme } from "@mui/system";

export default function ThemeSwitcher(): JSX.Element {
  const [currentMode, currentModeSetter] = useState(getCurrentMode);
  const currentTheme = useMemo(
    () => createTheme({ palette: { mode: currentMode } }),
    [currentMode],
  );
  return (
    <FormControlLabel
      className="w-fit px-2"
      control={
        <ThemeSwitch
          sx={{ m: 1 }}
          defaultChecked={isDarkMode()}
          onChange={(e) => {
            const newMode = e.target.checked ? "dark" : "light";
            currentModeSetter(newMode);
            changeTheme(newMode);
          }}
          theme={currentTheme}
        />
      }
      label=""
    />
  );
}

const ThemeSwitch = styled(Switch)(({ theme }) => ({
  padding: 8,
  "& .MuiSwitch-switchBase": {
    "&.Mui-checked": {
      color: "#fff",
    },
    "& + .MuiSwitch-track": {
      opacity: 1,
      backgroundColor: theme.palette.mode === "dark" ? "#8796A5" : "#e3e3e3",
    },
  },
  "& .MuiSwitch-track": {
    borderRadius: 22 / 2,
    backgroundColor: theme.palette.mode === "dark" ? "#383E44" : "#aab4be",
    "&:before, &:after": {
      content: '""',
      position: "absolute",
      top: "50%",
      transform: "translateY(-50%)",
      width: 16,
      height: 16,
    },
    "&:before": {
      // moon
      backgroundImage: `url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" height="16" width="16" viewBox="0 0 24 24"><path fill="${encodeURIComponent(
        "#fff",
      )}" d="M4.2 4l-1.9-.7-.6-1.8zm15 8.3a6.7 6.7 0 11-6.6-6.6 5.8 5.8 0 006.6 6.6z"/></svg>')`,
      left: 12,
    },
    "&:after": {
      // sun
      backgroundImage: `url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" height="16" width="16" viewBox="0 0 24 24"><path fill="${encodeURIComponent(
        "#000",
      )}" d="M9.305 1.667V3.75h1.389V1.667h-1.39zm-4.707 1.95l-.982.982L5.09 6.072l.982-.982-1.473-1.473zm10.802 0L13.927 5.09l.982.982 1.473-1.473-.982-.982zM10 5.139a4.872 4.872 0 00-4.862 4.86A4.872 4.872 0 0010 14.862 4.872 4.872 0 0014.86 10 4.872 4.872 0 0010 5.139zm0 1.389A3.462 3.462 0 0113.471 10a3.462 3.462 0 01-3.473 3.472A3.462 3.462 0 016.527 10 3.462 3.462 0 0110 6.528zM1.665 9.305v1.39h2.083v-1.39H1.666zm14.583 0v1.39h2.084v-1.39h-2.084zM5.09 13.928L3.616 15.4l.982.982 1.473-1.473-.982-.982zm9.82 0l-.982.982 1.473 1.473.982-.982-1.473-1.473zM9.305 16.25v2.083h1.389V16.25h-1.39z"/></svg>')`,
      right: 12,
    },
  },
  "& .MuiSwitch-thumb": {
    boxShadow: "none",
    width: 16,
    height: 16,
    margin: 2,
    backgroundColor: theme.palette.mode === "dark" ? "#F5F2F0" : "#fff",
  },
}));

const themeKey = "theme";
const darkModeMediaQuery = "(prefers-color-scheme: dark)";

export function changeTheme(theme: PaletteMode): void {
  localStorage.setItem(themeKey, theme);
  updateTheme();
}

function updateTheme(): void {
  if (isDarkMode()) {
    document.documentElement.classList.add("dark");
  } else {
    document.documentElement.classList.remove("dark");
  }
}

export function isDarkMode(): boolean {
  return (
    localStorage.getItem(themeKey) === "dark" ||
    (!(themeKey in localStorage) && window.matchMedia(darkModeMediaQuery).matches)
  );
}

export function getCurrentMode(): PaletteMode {
  return isDarkMode() ? "dark" : "light";
}
