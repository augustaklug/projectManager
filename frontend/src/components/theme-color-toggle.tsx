"use client"

import * as React from "react"
import { Paintbrush, Check } from "lucide-react"

import { Button } from "@/components/ui/button"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"

const themes = [
  { name: "Zinc", hue: 240, saturation: 5 },
  { name: "Slate", hue: 215, saturation: 20 },
  { name: "Stone", hue: 25, saturation: 5 },
  { name: "Gray", hue: 220, saturation: 10 },
  { name: "Neutral", hue: 0, saturation: 0 },
  { name: "Red", hue: 0, saturation: 75 },
  { name: "Rose", hue: 350, saturation: 90 },
  { name: "Orange", hue: 25, saturation: 95 },
  { name: "Green", hue: 140, saturation: 75 },
  { name: "Blue", hue: 220, saturation: 90 },
  { name: "Yellow", hue: 45, saturation: 95 },
  { name: "Violet", hue: 270, saturation: 80 },
]

export function ThemeColorToggle() {
  const [currentTheme, setCurrentTheme] = React.useState("Zinc")

  const setTheme = (themeName: string) => {
    const theme = themes.find(t => t.name === themeName)
    if (theme) {
      const { hue, saturation } = theme
      document.documentElement.style.setProperty('--primary-hue', hue.toString());
      document.documentElement.style.setProperty('--primary-saturation', `${saturation}%`);
      setCurrentTheme(themeName)
    }
  }

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button variant="outline" size="icon">
          <Paintbrush className="h-[1.2rem] w-[1.2rem]" />
          <span className="sr-only">Toggle theme color</span>
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end" className="w-56">
        {themes.map((theme) => (
          <DropdownMenuItem
            key={theme.name}
            onClick={() => setTheme(theme.name)}
            className="flex items-center justify-between"
          >
            <div className="flex items-center">
              <div
                className="w-4 h-4 rounded-full mr-2 border border-border"
                style={{
                  backgroundColor: `hsl(${theme.hue}, ${theme.saturation}%, 50%)`,
                }}
                aria-hidden="true"
              />
              <span>{theme.name}</span>
            </div>
            {currentTheme === theme.name && (
              <Check className="h-4 w-4 text-primary" />
            )}
          </DropdownMenuItem>
        ))}
      </DropdownMenuContent>
    </DropdownMenu>
  )
}